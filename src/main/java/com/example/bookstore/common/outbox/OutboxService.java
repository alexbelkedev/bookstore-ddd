package com.example.bookstore.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class OutboxService {

    private final OutboxRepository repo;
    private final ObjectMapper mapper;

    public OutboxService(OutboxRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public UUID append(String topic, String aggregateId, Object payload, Map<String, Object> headers) {
        try {
            String p = mapper.writeValueAsString(payload);
            String h = mapper.writeValueAsString(headers);
            var entity = new OutboxJpaEntity(UUID.randomUUID(), topic, aggregateId, p, h);
            repo.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to append outbox event", e);
        }
    }
}