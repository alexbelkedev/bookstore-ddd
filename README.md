# Book Store — DDD + Hexagonal (Spring Boot)

A minimal backend-only Book Store implemented with **Domain-Driven Design** (DDD) and **Hexagonal Architecture**.
Built for extension: add more bounded contexts (Ordering, Inventory), and later a web UI (React/Vue/Angular).

## Quick start

```bash
# 1) Build & run (Java 21)
mvn spring-boot:run

# 2) Try the API
curl -X POST http://localhost:8080/api/catalog/books \
  -H "Content-Type: application/json" \
  -d '{ "title":"Clean Architecture", "author":"Robert C. Martin", "isbn":"9780134494166", "price": { "amount":"34.99", "currency":"EUR" }, "stock": 10 }'

curl http://localhost:8080/api/catalog/books
```

## DDD/Hexagonal structure

```
src/main/java/com/example/bookstore
  ├─ BookstoreApplication.java           # bootstrap (composition root)
  └─ catalog                             # Bounded Context: Catalog
       ├─ domain                         # Pure domain: Entities, Value Objects, Repositories
       │    ├─ Book.java
       │    ├─ BookId.java
       │    ├─ Isbn.java
       │    ├─ Money.java
       │    └─ BookRepository.java
       ├─ application                    # Use-cases/orchestration
       │    └─ BookService.java
       ├─ infrastructure                 # Adapters to tech (DB, messaging, etc.)
       │    ├─ jpa
       │    │    ├─ BookJpaEntity.java
       │    │    ├─ SpringDataBookJpaRepository.java
       │    │    └─ BookRepositoryAdapter.java
       │    └─ config
       │         └─ CatalogConfig.java
       └─ api                            # HTTP API (REST)
            ├─ BookController.java
            ├─ dto
            │    ├─ CreateBookRequest.java
            │    └─ BookResponse.java
            └─ ApiExceptionHandler.java
```

**Rules:**
- `domain/` has **no Spring/JPA annotations**.
- `application/` uses domain interfaces and orchestrates use-cases.
- `infrastructure/` maps domain ↔ persistence (JPA entities, Spring Data).
- `api/` maps DTOs ↔ domain and exposes REST endpoints.

## Design choices

- **No shared database** with other contexts (when you add them later).
- **No direct cross-context imports** (use events/APIs instead).
- **Idempotent domain methods** (safe on retries).
- **Validation** at API boundary + domain invariants inside aggregates.

## Next steps

- Add pagination & filtering to GET /books.
- Add **Ordering** context (orders, order lines) and asynchronous integration.
- Replace H2 with Postgres, add Flyway migrations.
- Add OpenAPI (springdoc) and Dockerfile.
- Add frontend (React/Vue/Angular) consuming the REST API.

## License

MIT
