package com.example.user_service.controller;

import com.example.user_service.dto.UpdateProfileRequest;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.UserResponse;
import com.example.user_service.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final IProfileService profileService;

    @GetMapping("/{id}")
    UserResponse getProfile(@PathVariable("id") Long id) {
        return profileService.getProfileById(id);
    }

    @GetMapping("/by-name/{name}")
    UserResponse getProfileByName(@PathVariable("name") String name) {
        return profileService.getProfileByName(name);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhotoByUserId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(profileService.getPhotoById(id));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    void updateProfile(
            @PathVariable("id") Long id,
            @RequestPart("data") UpdateProfileRequest data,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        profileService.updateProfileById(id, data, photo);
    }
}
