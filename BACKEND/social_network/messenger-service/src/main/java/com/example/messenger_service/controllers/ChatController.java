package com.example.messenger_service.controllers;

import com.example.messenger_service.datamodel.ParticipantDAO;
import com.example.messenger_service.dto.*;
import com.example.messenger_service.datamodel.ChatDAO;
import com.example.messenger_service.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/messenger")
public class ChatController {

    private final ChatService chatService;


    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> createChat(
            @RequestHeader("X-User-Id") Long userId,
            @RequestPart("data") CreateChatRequest req,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        ChatDAO chat = chatService.createGroupChat(req.name, userId, photo);
        return ResponseEntity.status(201).body(chat);
    }

    @PostMapping("/{chatId}/invite")
    public ResponseEntity<?> invite(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long chatId,
            @RequestBody InviteRequest req
    ) {
        chatService.inviteToGroup(chatId, userId, req.userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{chatId}/remove")
    public ResponseEntity<?> remove(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long chatId,
            @RequestBody InviteRequest req
    ) {
        chatService.removeParticipant(chatId, userId, req.userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(
            value = "/{chatId}/edit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> edit(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long chatId,
            @RequestPart("data") EditChatRequest req,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        chatService.editGroup(chatId, userId, req.name, photo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{chatId}/quit")
    public ResponseEntity<?> quit(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long chatId
    ) {
        chatService.quitChat(chatId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getUserChats(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chatService.getUserChats(userId));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<SendMessageDTO>> getChatMessages(
            @PathVariable("chatId") Long chatId
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chatService.getChatMessages(chatId));
    }

    @GetMapping("/{chatId}/participants")
    public ResponseEntity<List<ParticipantDTO>> getParticipantsByChatId(
            @PathVariable("chatId") Long chatId
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chatService.getParticipantsByChatId(chatId));
    }
}

