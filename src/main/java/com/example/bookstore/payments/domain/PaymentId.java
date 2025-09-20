package com.example.bookstore.payments.domain;

import java.util.Objects;
import java.util.UUID;

public final class PaymentId {
    private final UUID value;
    public PaymentId(UUID value){ this.value = Objects.requireNonNull(value); }
    public static PaymentId newId(){ return new PaymentId(UUID.randomUUID()); }
    public UUID value(){ return value; }
    @Override public boolean equals(Object o){ return o instanceof PaymentId p && value.equals(p.value); }
    @Override public int hashCode(){ return value.hashCode(); }
    @Override public String toString(){ return value.toString(); }
}