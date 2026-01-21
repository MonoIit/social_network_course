package com.example.friend_service.service;

import com.example.friend_service.client.ProfileClient;
import com.example.friend_service.datamodel.FriendDAO;
import com.example.friend_service.datamodel.FriendsRepository;
import com.example.friend_service.dto.FriendDTO;
import com.example.friend_service.dto.UserDTO;
import com.example.friend_service.event.FriendshipCreated;
import com.example.friend_service.event.FriendshipDeleted;
import com.example.friend_service.model.*;
import com.example.friend_service.properties.KafkaProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService implements IFriendService {
    private final FriendsRepository repository;
    private final ModelMapper mapper;
    private final KafkaTemplate<String, FriendshipCreated> friendshipCreatedKafkaTemplate;
    private final KafkaTemplate<String, FriendshipDeleted> friendshipDeletedKafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private final ProfileClient profileClient;

    @Override
    public List<UserDTO> getUserFriends(Long userId) {
        List<FriendDAO> friends = repository.findAllByIdUserId(userId);

        return friends.stream()
                .map(f -> {
                    UserDTO user = profileClient.getProfile(f.getId().getFriendId()).block();

                    if (user != null) {
                        user.setStatus(f.getStatus().toString());
                    }
                    return user;
                })
                .toList();
    }

    @Override
    public List<FriendDTO> getUserFriendsByStatus(Long userId, FriendshipStatus status) {

        List<FriendDAO> friends;
        if (status == null) {
            friends = repository.findAllByIdUserId(userId);
        } else {
            friends = repository.findAllByIdUserIdAndStatus(userId, status);
        }

        return friends.stream()
                .map(e -> mapper.map(e, FriendDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public FriendDTO createFriendship(Long userId, Long friendId) {
        validateIds(userId, friendId);

//        repository.findById(friendId).orElseThrow(() -> new UserNotFoundException("User not Found"));

        Optional<FriendDAO> existing = repository.findFirstByIdUserIdAndIdFriendId(userId, friendId);
        if (existing.isPresent()) {
            FriendshipStatus status = existing.get().getStatus();
            if (status == FriendshipStatus.APPROVED) {
                throw new FriendshipAlreadyExistsException("Friendship already exists");
            } else if (status == FriendshipStatus.BLOCKED) {
                throw new FriendshipIsBlockedException("You are blocked");
            }
        }

        FriendDAO userToFriend = new FriendDAO(userId, friendId, FriendshipStatus.SENT);
        FriendDAO friendToUser = new FriendDAO(friendId, userId, FriendshipStatus.RECEIVED);

        try {
            repository.saveAll(List.of(userToFriend, friendToUser));
            friendshipCreatedKafkaTemplate.send(
                    kafkaProperties.getTopics().get("friendshipCreated"),
                    new FriendshipCreated(userId, friendId));
            return mapper.map(userToFriend, FriendDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Friendship is already exists" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteFriendship(Long userId, Long friendId) {
        repository.deleteByIdUserIdAndIdFriendId(userId, friendId);
        repository.deleteByIdUserIdAndIdFriendId(friendId, userId);
        friendshipDeletedKafkaTemplate.send(
                kafkaProperties.getTopics().get("friendshipDeleted"),
                new FriendshipDeleted(userId, friendId));
    }

    @Override
    @Transactional
    public void updateFriendshipStatus(Long userId, Long friendId, FriendshipStatus status) {
        validateIds(userId, friendId);
        doUpdateFriendshipStatus(userId, friendId, status, status);
    }

    @Override
    @Transactional
    public void declineFriendship(Long userId, Long friendId) {
        validateIds(userId, friendId);
        doUpdateFriendshipStatus(userId, friendId, FriendshipStatus.DECLINED, FriendshipStatus.DECLINE);
    }

    @Override
    @Transactional
    public void blockFriendship(Long userId, Long friendId) {
        validateIds(userId, friendId);
        doUpdateFriendshipStatus(userId, friendId, FriendshipStatus.BLOCKED, FriendshipStatus.BLOCK);
    }

    @Transactional
    private void doUpdateFriendshipStatus(
            Long userId,
            Long friendId,
            FriendshipStatus userToFriendStatus,
            FriendshipStatus friendToUserStatus
    ) {
        FriendDAO firstDAO = new FriendDAO(userId, friendId, userToFriendStatus);
        FriendDAO secondDAO = new FriendDAO(friendId, userId, friendToUserStatus);
        repository.save(firstDAO);
        repository.save(secondDAO);
    }

    private void validateIds(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new BadFriendshipRequest("invalid input params");
        }
    }
}
