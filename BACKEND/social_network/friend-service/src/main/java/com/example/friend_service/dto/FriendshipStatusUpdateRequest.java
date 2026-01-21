package com.example.friend_service.dto;

import com.example.friend_service.model.FriendshipStatus;
import lombok.Data;

@Data
public class FriendshipStatusUpdateRequest {

    private FriendshipStatus status;

}
