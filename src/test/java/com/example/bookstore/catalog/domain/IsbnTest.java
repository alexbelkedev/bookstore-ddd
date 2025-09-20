package com.example.bookstore.catalog.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsbnTest {

    @Test
    void valid_isbn_10_and_13() {
        assertDoesNotThrow(() -> new Isbn("0134494164"));      // 10 digits
        assertDoesNotThrow(() -> new Isbn("9780134494166"));   // 13 digits
        assertDoesNotThrow(() -> new Isbn("978-0-134-494166")); // hyphens/spaces allowed, length check simplified
    }

    @Test
    void invalid_isbn() {
        assertThrows(IllegalArgumentException.class, () -> new Isbn(""));
        assertThrows(IllegalArgumentException.class, () -> new Isbn("abc"));
        assertThrows(IllegalArgumentException.class, () -> new Isbn("123456789")); // 9 digits
    }
}
