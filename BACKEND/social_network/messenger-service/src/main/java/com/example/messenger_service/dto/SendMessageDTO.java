package com.example.messenger_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SendMessageDTO {
    public Long chatId;
    public Long senderId;
    public String text;
    public Instant sentAt;
}
