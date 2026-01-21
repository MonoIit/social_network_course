package com.example.messenger_service.datamodel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantDAO, Long> {
    List<ParticipantDAO> findByChatId(Long chatId);

    List<ParticipantDAO> findByUserId(Long userId);

    Optional<ParticipantDAO> findByChatIdAndUserIdNot(Long chatId, Long userId);

    Optional<ParticipantDAO> findByChatIdAndUserId(Long chatId, Long userId);

    void deleteByChatIdAndUserId(Long chatId, Long userId);
}
