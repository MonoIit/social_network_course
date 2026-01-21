package com.example.user_service.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String login;
    private String email;
}
