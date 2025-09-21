package com.example.bookstore.common.events.ordering;

public record OrderPlacedPayload(
        String orderId,
        String amount,
        String currency
) {
}