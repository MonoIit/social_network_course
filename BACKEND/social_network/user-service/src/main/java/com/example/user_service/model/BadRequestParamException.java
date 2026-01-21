package com.example.user_service.model;

public class BadRequestParamException extends RuntimeException {
    public BadRequestParamException(String message) {
        super(message);
    }
}
