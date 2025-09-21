// src/main/java/com/example/bookstore/catalog/infrastructure/jpa/BookQueriesJpa.java
package com.example.bookstore.catalog.infrastructure.jpa;

import com.example.bookstore.catalog.application.BookQueries;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class BookQueriesJpa implements BookQueries {
    private final EntityManager em;

    public BookQueriesJpa(EntityManager em) {
        this.em = em;
    }

    public PageResult search(Criteria c) {
        int page = Math.max(0, c.page());
        int size = Math.min(Math.max(1, c.size()), 100);
        String q = c.q() == null ? "" : c.q().trim().toLowerCase();
        String where = q.isEmpty() ? "" : " WHERE LOWER(b.title) LIKE :q OR LOWER(b.author) LIKE :q OR b.isbn LIKE :q ";

        var countQ = em.createQuery("SELECT COUNT(b) FROM BookJpaEntity b" + where, Long.class);
        var dataQ = em.createQuery("SELECT b FROM BookJpaEntity b" + where + " ORDER BY b.title ASC, b.author ASC", BookJpaEntity.class)
                .setFirstResult(page * size).setMaxResults(size);
        if (!q.isEmpty()) {
            countQ.setParameter("q", "%" + q + "%");
            dataQ.setParameter("q", "%" + q + "%");
        }

        long total = countQ.getSingleResult();
        var rows = dataQ.getResultList().stream().map(b ->
                new Row(b.getId().toString(), b.getTitle(), b.getAuthor(), b.getIsbn(),
                        b.getPriceAmount().toPlainString(), b.getPriceCurrency(), b.getStock())
        ).toList();
        return new PageResult(rows, total, page, size);
    }
}