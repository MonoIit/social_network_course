package com.example.messenger_service.controllers;

import com.example.messenger_service.dto.SendMessageDTO;
import com.example.messenger_service.datamodel.MessageDAO;
import com.example.messenger_service.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chats/{chatId}/sendMessage")
    public void sendMessage(@DestinationVariable Long chatId, @Payload SendMessageDTO dto) {
        MessageDAO saved = chatService.saveMessage(chatId, dto.senderId, dto.text, Instant.now());

        messagingTemplate.convertAndSend("/topic/chats/" + chatId, saved);

        messagingTemplate.convertAndSend("/topic/debug", "Сообщение отправлено: " + dto.text);
    }
}