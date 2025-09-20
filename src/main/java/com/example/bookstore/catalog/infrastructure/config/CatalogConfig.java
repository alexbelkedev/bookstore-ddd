package com.example.bookstore.catalog.infrastructure.config;

import com.example.bookstore.catalog.application.BookService;
import com.example.bookstore.catalog.domain.BookRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

    @Bean
    public BookService bookService(BookRepository repo) {
        return new BookService(repo);
    }
}
