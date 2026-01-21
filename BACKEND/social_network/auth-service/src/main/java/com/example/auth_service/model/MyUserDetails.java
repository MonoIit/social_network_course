package com.example.auth_service.model;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MyUserDetails extends User {
    private final Long userId;

    public MyUserDetails(String username, String password, Long userId) {
        super(username, password, List.of());
        this.userId = userId;
    }
}
