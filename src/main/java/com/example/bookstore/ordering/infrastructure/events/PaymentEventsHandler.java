package com.example.bookstore.ordering.infrastructure.events;

import com.example.bookstore.common.events.payments.PaymentAuthorizedPayload;
import com.example.bookstore.common.events.payments.PaymentFailedPayload;
import com.example.bookstore.common.outbox.OutboxDispatcher.OutboxEnvelope;
import com.example.bookstore.ordering.application.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventsHandler {

    private final ObjectMapper mapper;
    private final OrderService orders;

    public PaymentEventsHandler(ObjectMapper mapper, OrderService orders) {
        this.mapper = mapper;
        this.orders = orders;
    }

    @EventListener
    public void onOutbox(OutboxEnvelope env) throws Exception {
        switch (env.topic()) {
            case "payments.payment-authorized.v1" -> {
                var p = mapper.readValue(env.payload(), PaymentAuthorizedPayload.class);
                orders.markPaid(p.orderId());
            }
            case "payments.payment-failed.v1" -> {
                var p = mapper.readValue(env.payload(), PaymentFailedPayload.class);
                orders.markPaymentFailed(p.orderId(), p.reason());
            }
            default -> { /* ignore */ }
        }
    }
}