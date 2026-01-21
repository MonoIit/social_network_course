package com.example.messenger_service.dto;

import com.example.messenger_service.model.ChatRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ParticipantDTO {
    public Long userId;
}
