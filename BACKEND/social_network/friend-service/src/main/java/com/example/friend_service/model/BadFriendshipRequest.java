package com.example.friend_service.model;

public class BadFriendshipRequest extends RuntimeException {
    public BadFriendshipRequest(String message) {
        super(message);
    }
}
