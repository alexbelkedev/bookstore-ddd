package com.example.bookstore.catalog.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity for Book aggregate.
 */
public final class BookId {
    private final UUID value;

    public BookId(UUID value) {
        this.value = Objects.requireNonNull(value, "BookId value must not be null");
    }

    public static BookId newId() {
        return new BookId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookId other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
