package com.example.user_service.service;

import com.example.user_service.dto.UpdateProfileRequest;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IProfileService {

    UserResponse getProfileById(Long id);

    void updateProfileById(Long id, UpdateProfileRequest request, MultipartFile photo);

    UserResponse getProfileByName(String name);

    byte[] getPhotoById(Long id);
}
