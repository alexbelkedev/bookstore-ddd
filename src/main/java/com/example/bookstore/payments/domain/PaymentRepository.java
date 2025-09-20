package com.example.bookstore.payments.domain;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByIdempotencyKey(String key);
}