package com.example.messenger_service.dto;

import com.example.messenger_service.model.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponse {
    private Long chatId;
    private Long friendId;
    private String name;
    private byte[] photo;
}
