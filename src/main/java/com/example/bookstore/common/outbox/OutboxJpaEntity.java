package com.example.bookstore.common.outbox;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox", indexes = {
        @Index(name = "ix_outbox_status_created", columnList = "status,createdAt")
})
public class OutboxJpaEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String topic;        // e.g. "orders.order-placed.v1"
    @Column(nullable = false)
    private String aggregateId;  // for diagnostics / routing
    @Column(nullable = false)
    private String payload;      // JSON
    @Column(nullable = false)
    private String headers;      // JSON (correlationId, causationId, etc.)
    @Column(nullable = false)
    private String status;       // NEW, PUBLISHED, FAILED
    @Column(nullable = false)
    private int attempts;
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    private OffsetDateTime lastAttemptAt;
    private String lastError;

    protected OutboxJpaEntity() {
    }

    public OutboxJpaEntity(UUID id, String topic, String aggregateId, String payload, String headers) {
        this.id = id;
        this.topic = topic;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.headers = headers;
        this.status = "NEW";
        this.attempts = 0;
        this.createdAt = OffsetDateTime.now();
    }

    // getters/setters ...
    public UUID getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getPayload() {
        return payload;
    }

    public String getHeaders() {
        return headers;
    }

    public String getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }

    public String getLastError() {
        return lastError;
    }

    public void markAttempt(String error) {
        this.attempts++;
        this.lastAttemptAt = OffsetDateTime.now();
        this.lastError = error;
        this.status = (error == null ? "PUBLISHED" : "FAILED");
    }
}