package com.example.bookstore.dev;

import com.example.bookstore.catalog.application.BookService;
import com.example.bookstore.catalog.domain.Money;
import com.example.bookstore.ordering.application.OrderService;
import com.example.bookstore.ordering.domain.OrderLine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@Profile("dev")
public class SeedDataConfig {

    @Bean
    @Order(0)
    @Transactional
    CommandLineRunner seedBooks(BookService books, com.example.bookstore.catalog.domain.BookRepository repo) {
        return args -> {
            if (!repo.findAll().isEmpty()) return; // already seeded

            books.register("Clean Architecture", "Robert C. Martin", "9780134494166", "34.99", "EUR", 50);
            books.register("Domain-Driven Design", "Eric Evans", "9780321125217", "44.99", "EUR", 40);
            books.register("Implementing DDD", "Vaughn Vernon", "9780321834577", "49.99", "EUR", 30);
            books.register("Refactoring", "Martin Fowler", "9780201485677", "39.99", "EUR", 20);
            books.register("Test-Driven Development", "Kent Beck", "9780321146533", "29.99", "EUR", 25);
        };
    }

    @Bean
    @Order(1)
    @Transactional
    CommandLineRunner seedSampleOrder(
            com.example.bookstore.catalog.domain.BookRepository booksRepo,
            OrderService orders
    ) {
        return args -> {
            var book = booksRepo.findAll().stream().findFirst().orElse(null);
            if (book == null) return;

            // One small “paid” order to demonstrate the saga (total <= 1000)
            var line1 = new OrderLine(
                    book.id().value(),
                    book.title(),
                    2,
                    book.price()
            );
            orders.placeOrder(List.of(line1));

            // One “should fail” later (total > 1000) to showcase PAYMENT_FAILED
            var pricey = Money.of("600.00", "EUR");
            var line2 = new OrderLine(
                    book.id().value(),
                    book.title(),
                    2,
                    pricey
            );
            orders.placeOrder(List.of(line2));
        };
    }
}