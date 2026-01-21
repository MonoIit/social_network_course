package com.example.auth_service.model;

public class ConflictDataException extends RuntimeException {
    public ConflictDataException(String message) {
        super(message);
    }
}
