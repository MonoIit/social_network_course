package com.example.friend_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDeleted {
    Long firstUserId;
    Long secondUserId;
}
