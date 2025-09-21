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
- Integrated with Payments asynchronously via Outbox Saga
- API: `/api/orders`

### Payments
- Aggregate: `Payment`
- Simple rule: decline if amount > 1000
- Exposes `PaymentsFacade` (application layer)
- API: `/api/payments/authorize`

---

## Communication between contexts

Currently:

- **Ordering → Payments** uses **async event choreography** with a **Transactional Outbox**.
- Flow:
    - `OrderPlaced` event → Payments consumes → emits `PaymentAuthorized` or `PaymentFailed`.
    - Ordering consumes payment events → updates status.
- **No cross-domain dependencies**: only event contracts are shared, not classes.

This proves the **Ports & Adapters pattern** in action:

- Domain code only depends on abstractions.
- Infrastructure (HTTP, Events, Outbox) can change without touching the domain.

---

## Inspecting data in H2 console

Spring Boot enables the H2 web console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console).  
Connection settings:

- **JDBC URL**: `jdbc:h2:mem:bookstore`
- **User**: `sa`
- **Password**: (leave empty)

### Useful queries

📚 List all books

```sql
SELECT * FROM BOOKS;
```

🛒 List all orders

```sql
SELECT * FROM ORDERS;
```

📦 Order lines

```sql
SELECT * FROM ORDER_LINES;
```

📬 Inspect outbox events

```sql
SELECT ID, TOPIC, AGGREGATEID, STATUS, ATTEMPTS, CREATEDAT
FROM OUTBOX
ORDER BY CREATEDAT DESC;
```

💳 Payments table

```sql
SELECT * FROM PAYMENTS;
```

---

## DDD/Hexagonal rules

- **domain/** → pure business logic (no Spring/JPA annotations)
- **application/** → use-cases, orchestrating domain and ports
- **infrastructure/** → adapters (JPA, HTTP, Outbox, etc.)
- **api/** → REST controllers & DTOs

Bounded contexts are independent and communicate only via **ports** or **events**.

---

## Next milestones

- [ ] Add Inventory context (stock reservation before payment)
- [ ] Add retries + Dead Letter Queue for Outbox
- [ ] Hardening: Postgres + Flyway, OpenAPI annotations, Dockerfile
- [ ] Frontend app (React/Vue/Angular) consuming the REST API

---

## License

MIT
