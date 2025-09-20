package com.example.bookstore.ordering.domain;

import com.example.bookstore.catalog.domain.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final OrderId id;
    private final List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

    public Order(OrderId id, java.util.List<OrderLine> lines) {
        this.id = java.util.Objects.requireNonNull(id);
        if (lines == null || lines.isEmpty()) throw new IllegalArgumentException("order must have lines");
        this.lines.addAll(lines);
    }

    public static Order createNew(List<OrderLine> lines) {
        return new Order(OrderId.newId(), lines);
    }

    public OrderId id(){ return id; }
    public List<OrderLine> lines(){ return List.copyOf(lines); }
    public OrderStatus status(){ return status; }

    public Money total() {
        return lines.stream().map(OrderLine::lineTotal)
                .reduce(Money::add)
                .orElseThrow();
    }

    public void markPaid() { this.status = OrderStatus.PAID; }
    public void markPaymentFailed() { this.status = OrderStatus.PAYMENT_FAILED; }
}