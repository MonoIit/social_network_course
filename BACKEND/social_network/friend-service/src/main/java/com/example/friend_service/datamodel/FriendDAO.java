package com.example.friend_service.datamodel;

import com.example.friend_service.model.FriendshipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendDAO {

    @EmbeddedId
    private FriendId id;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public FriendDAO(Long userId, Long friendId, FriendshipStatus status) {
        this.id = new FriendId(userId, friendId);
        this.status = status;
    }
}
