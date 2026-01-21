package com.example.friend_service.model;

public class FriendshipIsBlockedException extends RuntimeException {
    public FriendshipIsBlockedException(String message) {
        super(message);
    }
}
