package com.example.messenger_service.datamodel;

import com.example.messenger_service.model.ChatRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private ChatRole role;

    private Instant addedAt = Instant.now();

    public ParticipantDAO(Long chatId, Long userId, ChatRole role) {
        this.chatId = chatId;
        this.userId = userId;
        this.role = role;
    }
}
