package com.example.bookstore.catalog.infrastructure.jpa;

import com.example.bookstore.catalog.domain.*;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookRepositoryAdapter implements BookRepository {

    private final SpringDataBookJpaRepository jpa;

    public BookRepositoryAdapter(SpringDataBookJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return jpa.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Book> findByIsbn(Isbn isbn) {
        return jpa.findByIsbn(isbn.value()).map(this::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Book save(Book book) {
        BookJpaEntity entity = toEntity(book);
        BookJpaEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    private Book toDomain(BookJpaEntity e) {
        Book d = Book.createNew(
            e.getTitle(),
            e.getAuthor(),
            new Isbn(e.getIsbn()),
            Money.of(e.getPriceAmount().toPlainString(), e.getPriceCurrency()),
            e.getStock()
        );
        // overwrite generated id with persisted one
        try {
            java.lang.reflect.Field idField = Book.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(d, new BookId(e.getId()));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to set Book.id via reflection", ex);
        }
        return d;
    }

    private BookJpaEntity toEntity(Book d) {
        UUID id = d.id().value();
        return new BookJpaEntity(
            id,
            d.title(),
            d.author(),
            d.isbn().value(),
            d.price().amount(),
            d.price().currency().getCurrencyCode(),
            d.stock()
        );
    }
}
