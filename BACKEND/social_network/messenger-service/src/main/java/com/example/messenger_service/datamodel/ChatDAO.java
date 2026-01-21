package com.example.messenger_service.datamodel;

import com.example.messenger_service.model.ChatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    private Long creatorId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    private Instant createdAt = Instant.now();

    public ChatDAO(String name, ChatType chatType, Long creatorId, byte[] photo) {
        this.name = name;
        this.chatType = chatType;
        this.creatorId = creatorId;
        this.photo = photo;
    }
}
