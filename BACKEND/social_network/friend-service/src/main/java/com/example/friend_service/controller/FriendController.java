package com.example.friend_service.controller;

import com.example.friend_service.dto.FriendDTO;
import com.example.friend_service.dto.FriendshipStatusUpdateRequest;
import com.example.friend_service.dto.UserDTO;
import com.example.friend_service.model.FriendshipStatus;
import com.example.friend_service.service.IFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/friends")
public class FriendController {
    private final IFriendService friendService;

    @GetMapping
    public List<UserDTO> getAllUserFriends(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return friendService.getUserFriends(userId);
    }

    @GetMapping("/requests")
    public List<FriendDTO> getUserRequests(
            @RequestParam(value = "status", required = false) FriendshipStatus status,
            @RequestHeader("X-User-Id") Long userId
    ) {
        return friendService.getUserFriendsByStatus(userId, status);
    }

    @PostMapping("/{friendId}/requests")
    public FriendDTO sendFriendshipRequest(
            @PathVariable("friendId") Long friendId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        return friendService.createFriendship(userId, friendId);
    }

    @PatchMapping("/{friendId}/requests")
    public void updateFriendshipRequest(
            @PathVariable("friendId") Long friendId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody FriendshipStatusUpdateRequest request
    ) {
        friendService.updateFriendshipStatus(userId, friendId, request.getStatus());
    }

    @PatchMapping("/{friendId}/requests/decline")
    public void declineFriendshipRequest(
            @PathVariable("friendId") Long friendId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        friendService.declineFriendship(userId, friendId);
    }

    @PatchMapping("/{friendId}/requests/block")
    public void blockFriendshipRequest(
            @PathVariable("friendId") Long friendId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        friendService.blockFriendship(userId, friendId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriendship(
            @PathVariable("friendId") Long friendId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        friendService.deleteFriendship(userId, friendId);
    }
}
