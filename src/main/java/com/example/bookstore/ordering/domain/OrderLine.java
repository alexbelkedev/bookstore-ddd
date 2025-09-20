package com.example.bookstore.ordering.domain;

import com.example.bookstore.catalog.domain.Money;

import java.util.Objects;
import java.util.UUID;

/** Value object representing a line snapshot (book + price at purchase). */
public final class OrderLine {
    private final UUID bookId;
    private final String title;
    private final int quantity;
    private final Money unitPrice;

    public OrderLine(UUID bookId, String title, int quantity, Money unitPrice) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity > 0 required");
        this.bookId = Objects.requireNonNull(bookId);
        this.title = Objects.requireNonNull(title);
        this.quantity = quantity;
        this.unitPrice = Objects.requireNonNull(unitPrice);
    }
    public UUID bookId(){ return bookId; }
    public String title(){ return title; }
    public int quantity(){ return quantity; }
    public Money unitPrice(){ return unitPrice; }

    public Money lineTotal() { return unitPrice.multiply(quantity); }
}