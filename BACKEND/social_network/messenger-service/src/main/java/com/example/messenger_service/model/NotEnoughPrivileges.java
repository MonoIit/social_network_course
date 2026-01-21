package com.example.messenger_service.model;

public class NotEnoughPrivileges extends RuntimeException {
    public NotEnoughPrivileges(String message) {
        super(message);
    }
}
