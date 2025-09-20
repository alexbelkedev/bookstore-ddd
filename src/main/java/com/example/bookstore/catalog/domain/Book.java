package com.example.bookstore.catalog.domain;

import java.util.Objects;

/**
 * Aggregate Root for a Book in the Catalog.
 */
public class Book {
    private final BookId id;
    private String title;
    private String author;
    private final Isbn isbn;
    private Money price;
    private int stock;

    private Book(BookId id, String title, String author, Isbn isbn, Money price, int stock) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = requireNonEmpty(title, "title");
        this.author = requireNonEmpty(author, "author");
        this.isbn = Objects.requireNonNull(isbn, "isbn");
        this.price = Objects.requireNonNull(price, "price");
        if (stock < 0) throw new IllegalArgumentException("stock must be >= 0");
        this.stock = stock;
    }

    public static Book createNew(String title, String author, Isbn isbn, Money price, int stock) {
        return new Book(BookId.newId(), title, author, isbn, price, stock);
    }

    public BookId id() { return id; }
    public String title() { return title; }
    public String author() { return author; }
    public Isbn isbn() { return isbn; }
    public Money price() { return price; }
    public int stock() { return stock; }

    public void rename(String newTitle) {
        this.title = requireNonEmpty(newTitle, "title");
    }

    public void changeAuthor(String newAuthor) {
        this.author = requireNonEmpty(newAuthor, "author");
    }

    public void changePrice(Money newPrice) {
        this.price = Objects.requireNonNull(newPrice, "price");
    }

    public void addStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        this.stock += qty;
    }

    public void removeStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (qty > stock) throw new IllegalStateException("not enough stock");
        this.stock -= qty;
    }

    private static String requireNonEmpty(String s, String field) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(field + " must not be blank");
        return s.strip();
    }
}
