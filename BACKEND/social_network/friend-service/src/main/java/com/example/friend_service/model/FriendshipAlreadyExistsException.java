package com.example.friend_service.model;

public class FriendshipAlreadyExistsException extends RuntimeException {
    public FriendshipAlreadyExistsException(String message) {
        super(message);
    }
}
