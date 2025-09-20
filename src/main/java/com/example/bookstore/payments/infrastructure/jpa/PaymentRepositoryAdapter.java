package com.example.bookstore.payments.infrastructure.jpa;

import com.example.bookstore.payments.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {
    private final SpringDataPaymentJpaRepository jpa;

    public PaymentRepositoryAdapter(SpringDataPaymentJpaRepository jpa) { this.jpa = jpa; }

    @Override
    public Payment save(Payment p) {
        var e = new PaymentJpaEntity(
                p.id().value(), p.orderId(), p.amount(), p.currency(), p.idempotencyKey(), p.status().name()
        );
        jpa.save(e);
        return p;
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String key) {
        return jpa.findByIdempotencyKey(key).map(e -> {
            var d = Payment.newAuth(e.getOrderId(), e.getAmount(), e.getCurrency(), e.getIdempotencyKey());
            try {
                var idField = Payment.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(d, new PaymentId(e.getId()));
                var statusField = Payment.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(d, PaymentStatus.valueOf(e.getStatus()));
            } catch (Exception ex) { throw new RuntimeException(ex); }
            return d;
        });
    }
}