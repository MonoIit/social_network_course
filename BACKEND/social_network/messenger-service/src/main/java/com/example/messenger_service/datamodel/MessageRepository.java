package com.example.messenger_service.datamodel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageDAO, Long> {
    List<MessageDAO> findTop100ByChatIdOrderByTimestampDesc(Long chatId);

    List<MessageDAO> findByChatIdOrderByTimestampAsc(Long chatId);
}
