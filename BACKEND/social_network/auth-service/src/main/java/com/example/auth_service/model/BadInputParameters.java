package com.example.auth_service.model;

public class BadInputParameters extends RuntimeException {
    public BadInputParameters(String message) {
        super(message);
    }
}
