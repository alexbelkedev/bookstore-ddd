package com.example.bookstore.catalog.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataBookJpaRepository extends JpaRepository<BookJpaEntity, UUID> {
    Optional<BookJpaEntity> findByIsbn(String isbn);
}
