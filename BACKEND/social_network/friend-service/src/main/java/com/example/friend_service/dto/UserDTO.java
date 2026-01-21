package com.example.friend_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String login;

    private String email;

    private String status;
}
