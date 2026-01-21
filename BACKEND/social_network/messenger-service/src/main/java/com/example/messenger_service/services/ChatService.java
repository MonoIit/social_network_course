package com.example.messenger_service.services;

import com.example.messenger_service.datamodel.*;
import com.example.messenger_service.dto.ChatResponse;
import com.example.messenger_service.dto.ParticipantDTO;
import com.example.messenger_service.dto.SendMessageDTO;
import com.example.messenger_service.events.FriendshipCreated;
import com.example.messenger_service.events.UserCreated;
import com.example.messenger_service.model.ChatRole;
import com.example.messenger_service.model.ChatType;
import com.example.messenger_service.model.NotEnoughPrivileges;
import org.apache.catalina.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ParticipantRepository participantRepository;
    private final MessageRepository messageRepository;

    public ChatService(ChatRepository chatRepository,
                       ParticipantRepository participantRepository,
                       MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
    }

    public List<ChatResponse> getUserChats(Long userId) {
        List<ParticipantDAO> userChats = participantRepository.findByUserId(userId);

        List<ChatResponse> response = new ArrayList<>();
        for (ParticipantDAO dao : userChats) {
            ChatDAO chatDAO = chatRepository.findById(dao.getChatId()).get();
            if (!chatDAO.getChatType().equals(ChatType.GROUP)) {
                var friendChat = participantRepository.findByChatIdAndUserIdNot(dao.getChatId(), userId)
                        .orElseThrow(() -> new RuntimeException("no chat friends with id " + dao.getChatId()));

                response.add(new ChatResponse(
                        friendChat.getChatId(),
                        friendChat.getUserId(),
                        null,
                        null
                ));
            } else {
                response.add(new ChatResponse(
                        chatDAO.getId(),
                        -1L,
                        chatDAO.getName(),
                        chatDAO.getPhoto()
                ));
            }
        }

        return response;
    }

    @Transactional
    public ChatDAO createGroupChat(String name, Long creatorId, MultipartFile photo) throws IOException {
        ChatDAO chat = new ChatDAO(name, ChatType.GROUP, creatorId, photo.getBytes());
        ChatDAO saved = chatRepository.save(chat);

        ParticipantDAO p = new ParticipantDAO(saved.getId(), creatorId, ChatRole.ADMIN);
        participantRepository.save(p);
        return saved;
    }

    @Transactional
    @KafkaListener(topics = "${kafka.topics.friendshipCreated}",
            groupId = "${kafka.group.id}",
            containerFactory = "createdFriendshipKafkaListenerContainerFactory"
    )
    public ChatDAO createPrivateChat(FriendshipCreated friendshipCreated) {
        Long userA = friendshipCreated.getFirstUserId();
        Long userB = friendshipCreated.getSecondUserId();
        System.out.println("here");
        ChatDAO chat = new ChatDAO(null, ChatType.PRIVATE, null, null);
        ChatDAO saved = chatRepository.save(chat);

        ParticipantDAO p1 = new ParticipantDAO(saved.getId(), userA, ChatRole.PARTICIPANT);
        ParticipantDAO p2 = new ParticipantDAO(saved.getId(), userB, ChatRole.PARTICIPANT);
        participantRepository.save(p1);
        participantRepository.save(p2);
        return saved;
    }

//    @Transactional
//    @KafkaListener(topics = "${kafka.topics.userCreated}",
//            groupId = "${kafka.group.id}",
//            containerFactory = "createdUserKafkaListenerContainerFactory"
//    )
//    public ChatDAO createPersonalChat(UserCreated user) {
//        Long userId = user.getId();
//        ChatDAO chat = new ChatDAO("favorite", ChatType.PERSONAL, userId, null);
//        ChatDAO saved = chatRepository.save(chat);
//
//        ParticipantDAO p1 = new ParticipantDAO(saved.getId(), userId, ChatRole.PARTICIPANT);
//        participantRepository.save(p1);
//        return saved;
//    }

    @Transactional
    public void inviteToGroup(Long chatId, Long inviterId, Long newUserId) {
        boolean isAdmin = checkAdminPrivalage(chatId, inviterId);
        if (!isAdmin) {
            throw new RuntimeException("Only admin can invite participants");
        }

        ParticipantDAO p = new ParticipantDAO(chatId, newUserId, ChatRole.PARTICIPANT);
        participantRepository.save(p);
    }

    @Transactional
    public void removeParticipant(Long chatId, Long byUserId, Long userIdToRemove) {
        boolean isAdmin = checkAdminPrivalage(chatId, byUserId);
        if (!isAdmin) {
            throw new RuntimeException("Only admin can remove participants");
        }

        participantRepository.deleteByChatIdAndUserId(chatId, userIdToRemove);
    }

    @Transactional
    public void editGroup(Long chatId, Long editorId, String newName, MultipartFile newPhoto) throws IOException {
        ChatDAO chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        if (ChatType.GROUP != chat.getChatType()) throw new RuntimeException("Not group chat");
        if (!chat.getCreatorId().equals(editorId)) throw new NotEnoughPrivileges("Only creator can edit");
        if (newName != null) chat.setName(newName);
        if (newPhoto != null) chat.setPhoto(newPhoto.getBytes());
        chatRepository.save(chat);
    }

    @Transactional
    public void quitChat(Long chatId, Long userId) {
        Optional<ParticipantDAO> p = participantRepository.findByChatIdAndUserId(chatId, userId);
        if (p.isEmpty()) return;
        participantRepository.deleteByChatIdAndUserId(chatId, userId);

        ChatDAO chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) return;

        if (chat.getCreatorId().equals(userId)) {
            List<ParticipantDAO> participants = participantRepository.findByChatId(chatId);
            if (participants.isEmpty()) {
                messageRepository.deleteAll(messageRepository.findByChatIdOrderByTimestampAsc(chatId));
                participantRepository.findByChatId(chatId).forEach(participantRepository::delete);
                chatRepository.delete(chat);
            } else {
                participants.sort(Comparator.comparing(ParticipantDAO::getUserId));
                ParticipantDAO newCreator = participants.getFirst();
                chat.setCreatorId(newCreator.getUserId());

                newCreator.setRole(ChatRole.ADMIN);
                participantRepository.save(newCreator);
                chatRepository.save(chat);
            }
        }
    }

    @Transactional
    public MessageDAO saveMessage(Long chatId, Long senderId, String text, Instant sentAt) {
        MessageDAO m = new MessageDAO();
        m.setChatId(chatId);
        m.setSenderId(senderId);
        m.setText(text);
        m.setTimestamp(sentAt);
        return messageRepository.save(m);
    }

    public boolean isParticipant(Long chatId, Long userId) {
        return participantRepository.findByChatIdAndUserId(chatId, userId).isPresent();
    }

    private boolean checkAdminPrivalage(Long chatId, Long userId) {
        Optional<ParticipantDAO> by = participantRepository.findByChatIdAndUserId(chatId, userId);
        if (by.isEmpty()) throw new RuntimeException("Not participant");
        return ChatRole.ADMIN == by.get().getRole();
    }

    public List<SendMessageDTO> getChatMessages(Long chatId) {
        return messageRepository.findTop100ByChatIdOrderByTimestampDesc(chatId).stream()
                .map(m -> new SendMessageDTO(
                        chatId,
                        m.getSenderId(),
                        m.getText(),
                        m.getTimestamp()
                ))
                .toList();
    }

    public List<ParticipantDTO> getParticipantsByChatId(Long chatId) {
        return participantRepository.findByChatId(chatId).stream()
                .map(dao -> new ParticipantDTO(dao.getUserId()))
                .toList();
    }
}
