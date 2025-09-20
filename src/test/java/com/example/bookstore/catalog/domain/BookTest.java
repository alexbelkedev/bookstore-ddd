package com.example.bookstore.catalog.domain;

import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void stock_invariants() {
        Book b = Book.createNew("TDD", "Kent Beck", new Isbn("9780135974445"),
            new Money(new java.math.BigDecimal("19.99"), Currency.getInstance("EUR")), 0);

        assertThrows(IllegalArgumentException.class, () -> b.removeStock(1));
        b.addStock(5);
        assertEquals(5, b.stock());
        b.removeStock(2);
        assertEquals(3, b.stock());
        assertThrows(IllegalArgumentException.class, () -> b.addStock(0));
    }
}
