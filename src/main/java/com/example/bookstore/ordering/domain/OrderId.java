package com.example.bookstore.ordering.domain;

import java.util.Objects;
import java.util.UUID;

public final class OrderId {
    private final UUID value;

    public OrderId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }
    public static OrderId newId() { return new OrderId(UUID.randomUUID()); }
    public UUID value() { return value; }

    @Override public boolean equals(Object o){ return o instanceof OrderId other && value.equals(other.value); }
    @Override public int hashCode(){ return value.hashCode(); }
    @Override public String toString(){ return value.toString(); }
}