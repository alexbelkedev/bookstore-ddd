package com.example.bookstore.common.events.payments;

public record PaymentFailedPayload(
        String orderId,
        String reason
) {
}