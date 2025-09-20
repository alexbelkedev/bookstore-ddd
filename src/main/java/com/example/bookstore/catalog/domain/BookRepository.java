package com.example.bookstore.catalog.domain;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(BookId id);
    Optional<Book> findByIsbn(Isbn isbn);
    List<Book> findAll();
    Book save(Book book);
}
