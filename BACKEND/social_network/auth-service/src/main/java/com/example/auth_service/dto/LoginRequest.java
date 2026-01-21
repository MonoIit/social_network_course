package com.example.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @JsonProperty(value = "login", required = true)
    private final String login;

    @JsonProperty(value = "password", required = true)
    private final String password;

}