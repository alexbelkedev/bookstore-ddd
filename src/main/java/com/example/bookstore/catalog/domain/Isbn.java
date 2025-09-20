package com.example.bookstore.catalog.domain;

import java.util.Objects;

/**
 * Value Object representing ISBN-10/13 (simplified validation).
 */
public final class Isbn {
    private final String value;

    public Isbn(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISBN must not be blank");
        }
        String normalized = value.replaceAll("[\s-]", "");
        if (!(normalized.matches("\\d{10}") || normalized.matches("\\d{13}"))) {
            throw new IllegalArgumentException("ISBN must be 10 or 13 digits");
        }
        this.value = normalized;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Isbn other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override public String toString() { return value; }
}
