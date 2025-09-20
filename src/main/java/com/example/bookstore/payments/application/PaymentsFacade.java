package com.example.bookstore.payments.application;

import com.example.bookstore.payments.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentsFacade {

    private final PaymentRepository repo;

    public record Result(boolean authorized, String paymentId) {}

    public PaymentsFacade(PaymentRepository repo) { this.repo = repo; }

    @Transactional
    public Result authorize(String orderId, String amount, String currency, String idempotencyKey) {
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
        Objects.requireNonNull(idempotencyKey);

        // Idempotency
        var existing = repo.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            var p = existing.get();
            return new Result(p.status() == PaymentStatus.AUTHORIZED, p.id().toString());
        }

        var payment = Payment.newAuth(orderId, new BigDecimal(amount), currency, idempotencyKey);

        // Naive "authorization" rule for demo: decline if amount > 1000
        if (new BigDecimal(amount).compareTo(new BigDecimal("1000.00")) > 0) payment.decline();
        else payment.authorize();

        payment = repo.save(payment);
        return new Result(payment.status() == PaymentStatus.AUTHORIZED, payment.id().toString());
    }
}