package com.example.friend_service.service;

import com.example.friend_service.dto.FriendDTO;
import com.example.friend_service.dto.UserDTO;
import com.example.friend_service.model.FriendshipStatus;

import java.util.List;

public interface IFriendService {

    List<UserDTO> getUserFriends(Long userId);

    List<FriendDTO> getUserFriendsByStatus(Long userId, FriendshipStatus status);

    FriendDTO createFriendship(Long userId, Long friendId);

    void deleteFriendship(Long userId, Long friendId);

    void updateFriendshipStatus(Long userId, Long friendId, FriendshipStatus status);

    void declineFriendship(Long userId, Long friendId);

    void blockFriendship(Long userId, Long friendId);
}
