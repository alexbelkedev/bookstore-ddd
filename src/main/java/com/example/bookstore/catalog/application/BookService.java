package com.example.bookstore.catalog.application;

import com.example.bookstore.catalog.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Book register(String title, String author, String isbn, String amount, String currency, int stock) {
        Isbn isbnVO = new Isbn(isbn);
        if (repo.findByIsbn(isbnVO).isPresent()) {
            throw new IllegalArgumentException("Book with ISBN already exists: " + isbnVO.value());
        }
        Money price = new Money(new java.math.BigDecimal(amount), Currency.getInstance(currency));
        Book book = Book.createNew(title, author, isbnVO, price, stock);
        return repo.save(book);
    }

    @Transactional(readOnly = true)
    public Optional<Book> getById(String id) {
        return repo.findById(new BookId(java.util.UUID.fromString(id)));
    }

    @Transactional(readOnly = true)
    public List<Book> listAll() {
        return repo.findAll();
    }
}
