package com.example.friend_service.datamodel;

import com.example.friend_service.model.FriendshipStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends CrudRepository<FriendDAO, Long> {

    Optional<FriendDAO> findFirstByIdUserIdAndIdFriendId(Long userId, Long friendId);

    List<FriendDAO> findAllByIdUserId(Long userId);

    List<FriendDAO> findAllByIdUserIdAndStatus(Long userId, FriendshipStatus status);

    void deleteByIdUserIdAndIdFriendId(Long userId, Long friendId);
}
