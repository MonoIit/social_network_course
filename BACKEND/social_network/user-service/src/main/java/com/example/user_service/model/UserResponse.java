package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String login;
    private String email;
    private Boolean hasPhoto;
}
