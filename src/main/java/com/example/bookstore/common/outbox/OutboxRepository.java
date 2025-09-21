package com.example.bookstore.common.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxJpaEntity, UUID> {
    List<OutboxJpaEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);

    List<OutboxJpaEntity> findTop50ByStatusAndLastAttemptAtBeforeOrderByCreatedAtAsc(String status, OffsetDateTime before);
}