# Book Store — DDD + Hexagonal (Spring Boot)

A backend-only Book Store implemented with **Domain-Driven Design (DDD)** and **Hexagonal Architecture**.  
Built for extension: new bounded contexts (Ordering, Payments, Inventory) and later a web UI (React/Vue/Angular).

---

## Quick start

```bash
# build & run (Java 21, Maven)
mvn spring-boot:run

# create a book
curl -X POST http://localhost:8080/api/catalog/books   -H "Content-Type: application/json"   -d '{ "title":"Clean Architecture", "author":"Robert C. Martin",
        "isbn":"9780134494166",
        "price": { "amount":"34.99", "currency":"EUR" }, "stock": 10 }'

# list books
curl http://localhost:8080/api/catalog/books

# place an order (snapshot price/title)
BOOK_ID=<uuid-from-catalog-response>
curl -X POST http://localhost:8080/api/orders   -H "Content-Type: application/json"   -d "{
        \"lines\":[
          {
            \"bookId\":\"$BOOK_ID\",
            \"title\":\"Clean Architecture\",
            \"currency\":\"EUR\",
            \"unitAmount\":\"34.99\",
            \"quantity\":2
          }
        ]
      }"
```

Expected result:

- If total ≤ 1000.00 → `status: PAID`
- If total > 1000.00 → `status: PAYMENT_FAILED` (demo rule in Payments)

---

## Current bounded contexts

### Catalog

- Aggregate: `Book`
- Value Objects: `Isbn`, `Money`
- Repository: `BookRepository`
- API: `/api/catalog/books`

### Ordering

- Aggregate: `Order` (with `OrderLine`)
- Uses `PaymentsPort` to authorize payments
- API: `/api/orders`

### Payments

- Aggregate: `Payment`
- Simple rule: decline if amount > 1000
- Exposes `PaymentsFacade` (used locally by Ordering)
- API: `/api/payments/authorize` (already present, but not used yet)

---

## Communication between contexts

Currently:

- **Ordering → Payments** via a **local adapter** (`LocalPaymentsAdapter`) that calls `PaymentsFacade` directly.
- No direct entity sharing, only via ports.
- Each context has its own persistence model.

Planned:

- **Swap to HTTP adapter** (Ordering calls Payments over REST)
- Or introduce **async events** (`OrderPlaced` → `PaymentAuthorized/Failed`).

---

## DDD/Hexagonal rules

- **domain/** → pure business logic (no Spring/JPA annotations)
- **application/** → use-cases, orchestrating domain and ports
- **infrastructure/** → adapters (JPA, HTTP, local ACL)
- **api/** → REST controllers & DTOs

Bounded contexts are independent and communicate only via **ports**.

---

## Next milestones

- [ ] Swap Ordering → Payments communication to HTTP adapter
- [ ] Add async event flow (outbox, saga pattern)
- [ ] Add Inventory context (stock reservation before payment)
- [ ] Hardening: Postgres + Flyway, OpenAPI (springdoc), Dockerfile
- [ ] Frontend app (React/Vue/Angular) consuming the REST API

---

## License

MIT
