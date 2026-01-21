package com.example.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RegisterRequest extends LoginRequest {

    @JsonProperty(value = "email", required = true)
    private final String email;


    public RegisterRequest(String login, String password, String email) {
        super(login, password);
        this.email = email;
    }
}
