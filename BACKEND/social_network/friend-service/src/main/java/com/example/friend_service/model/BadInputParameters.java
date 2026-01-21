package com.example.friend_service.model;

public class BadInputParameters extends RuntimeException {
    public BadInputParameters(String message) {
        super(message);
    }
}
