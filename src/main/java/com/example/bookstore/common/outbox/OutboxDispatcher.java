package com.example.bookstore.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OutboxDispatcher {
    private final ApplicationEventPublisher publisher;
    private final ObjectMapper mapper;

    public OutboxDispatcher(ApplicationEventPublisher publisher, ObjectMapper mapper) {
        this.publisher = publisher;
        this.mapper = mapper;
    }

    public void dispatch(String topic, String payload, String headers) {
        // publish a simple envelope into the in-process bus
        publisher.publishEvent(new OutboxEnvelope(topic, payload, headers));
    }

    public record OutboxEnvelope(String topic, String payload, String headers) {
    }
}