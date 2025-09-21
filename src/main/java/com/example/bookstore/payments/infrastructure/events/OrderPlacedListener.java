package com.example.bookstore.payments.infrastructure.events;

import com.example.bookstore.common.events.ordering.OrderPlacedPayload;
import com.example.bookstore.common.events.payments.PaymentAuthorizedPayload;
import com.example.bookstore.common.events.payments.PaymentFailedPayload;
import com.example.bookstore.common.outbox.OutboxDispatcher.OutboxEnvelope;
import com.example.bookstore.common.outbox.OutboxService;
import com.example.bookstore.payments.application.PaymentsFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderPlacedListener {

    private final ObjectMapper mapper;
    private final PaymentsFacade payments;
    private final OutboxService outbox;

    public OrderPlacedListener(ObjectMapper mapper, PaymentsFacade payments, OutboxService outbox) {
        this.mapper = mapper;
        this.payments = payments;
        this.outbox = outbox;
    }

    @EventListener
    public void onOutbox(OutboxEnvelope env) throws Exception {
        if (!"orders.order-placed.v1".equals(env.topic())) return;

        var p = mapper.readValue(env.payload(), OrderPlacedPayload.class);
        var result = payments.authorize(p.orderId(), p.amount(), p.currency(), "order-" + p.orderId());

        if (result.authorized()) {
            outbox.append(
                    "payments.payment-authorized.v1", p.orderId(),
                    new PaymentAuthorizedPayload(p.orderId(), result.paymentId(), p.amount(), p.currency()),
                    Map.of("correlationId", p.orderId(), "causation", "payments.authorize")
            );
        } else {
            outbox.append(
                    "payments.payment-failed.v1", p.orderId(),
                    new PaymentFailedPayload(p.orderId(), "declined"), // expand with real reasons later
                    Map.of("correlationId", p.orderId(), "causation", "payments.authorize")
            );
        }
    }
}