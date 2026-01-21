package com.example.user_service.service;

import com.example.user_service.datamodel.UserDAO;
import com.example.user_service.datamodel.UsersRepository;
import com.example.user_service.dto.UpdateProfileRequest;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.event.UserCreated;
import com.example.user_service.event.UserDeleted;
import com.example.user_service.model.BadRequestParamException;
import com.example.user_service.model.ProfileNotFoundException;
import com.example.user_service.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.PropertyValueException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse getProfileById(Long id) {
        UserDAO user = usersRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(String.format("Profile this id %s not found", id)));

        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getPhoto() != null
        );
    }

    @Override
    public void updateProfileById(Long id, UpdateProfileRequest request, MultipartFile photo) {
        UserDAO user = usersRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(String.format("Profile this id %s not found", id)));

        try {
            user.setEmail(request.getEmail());
            user.setLogin(request.getLogin());
            user.setPhoto(photo == null ? null : photo.getBytes());
        } catch (PropertyValueException | IOException e) {
            throw new BadRequestParamException("Invalid request body");
        }

        usersRepository.save(user);
    }

    @Override
    public UserResponse getProfileByName(String name) {
        Iterable<UserDAO> users = usersRepository.findAll();

        for (UserDAO user : users) {
            if (user.getLogin().startsWith(name)) {
                return new UserResponse(
                        user.getId(),
                        user.getLogin(),
                        user.getEmail(),
                        user.getPhoto() == null
                );
            }
        }
        throw new ProfileNotFoundException("Not found profile with name " + name);
    }

    @KafkaListener(topics = "${kafka.topics.userCreated}",
            groupId = "${kafka.group.id}",
            containerFactory = "createdKafkaListenerContainerFactory"
    )
    private void createUser(UserCreated userCreated) {
        UserDAO user = new UserDAO(userCreated.getId(), userCreated.getLogin(), userCreated.getEmail());
        usersRepository.save(user);
    }

    @KafkaListener(topics = "${kafka.topics.userDeleted}",
            groupId = "${kafka.group.id}",
            containerFactory = "deletedKafkaListenerContainerFactory"
    )
    private void deleteUser(UserDeleted deletedEvent) {
        usersRepository.deleteById(deletedEvent.getId());
    }

    @Override
    public byte[] getPhotoById(Long id) {
        UserDAO user = usersRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(String.format("Profile this id %s not found", id)));

        return user.getPhoto();
    }
}
