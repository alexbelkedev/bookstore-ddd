package com.example.bookstore.common.events.payments;

public record PaymentAuthorizedPayload(
        String orderId,
        String paymentId,
        String amount,
        String currency
) {
}