package com.example.messenger_service.datamodel;

import com.example.messenger_service.model.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatDAO, Long> {
    List<ChatDAO> findByChatTypeAndCreatorId(ChatType chatType, Long creatorId);
}
