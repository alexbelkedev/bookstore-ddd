package com.example.bookstore.payments.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Payment {
    private final PaymentId id;
    private final String orderId;
    private final BigDecimal amount;
    private final String currency;
    private final String idempotencyKey;
    private PaymentStatus status;

    private Payment(PaymentId id, String orderId, BigDecimal amount, String currency, String idempotencyKey) {
        this.id = Objects.requireNonNull(id);
        this.orderId = Objects.requireNonNull(orderId);
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey);
        this.status = PaymentStatus.AUTHORIZED; // default, will be set explicitly
    }

    public static Payment newAuth(String orderId, BigDecimal amount, String currency, String key) {
        return new Payment(PaymentId.newId(), orderId, amount, currency, key);
    }

    public PaymentId id(){ return id; }
    public String orderId(){ return orderId; }
    public BigDecimal amount(){ return amount; }
    public String currency(){ return currency; }
    public String idempotencyKey(){ return idempotencyKey; }
    public PaymentStatus status(){ return status; }

    public void authorize() { this.status = PaymentStatus.AUTHORIZED; }
    public void decline() { this.status = PaymentStatus.DECLINED; }
}