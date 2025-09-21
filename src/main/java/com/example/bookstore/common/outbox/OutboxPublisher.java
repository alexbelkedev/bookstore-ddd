package com.example.bookstore.common.outbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Component
public class OutboxPublisher {

    private final OutboxRepository repo;
    private final OutboxDispatcher dispatcher;

    public OutboxPublisher(OutboxRepository repo, OutboxDispatcher dispatcher) {
        this.repo = repo;
        this.dispatcher = dispatcher;
    }

    @Scheduled(fixedDelay = 1000)  // every 1s
    @Transactional
    public void publishNew() {
        repo.findTop50ByStatusOrderByCreatedAtAsc("NEW").forEach(this::publishOne);
        // retry previously failed after 30s
        repo.findTop50ByStatusAndLastAttemptAtBeforeOrderByCreatedAtAsc("FAILED", OffsetDateTime.now().minusSeconds(30))
                .forEach(this::publishOne);
    }

    private void publishOne(OutboxJpaEntity e) {
        try {
            dispatcher.dispatch(e.getTopic(), e.getPayload(), e.getHeaders());
            e.markAttempt(null);
        } catch (Exception ex) {
            e.markAttempt(ex.getMessage());
        }
    }
}