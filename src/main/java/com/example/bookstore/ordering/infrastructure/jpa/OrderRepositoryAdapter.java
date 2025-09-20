package com.example.bookstore.ordering.infrastructure.jpa;

import com.example.bookstore.catalog.domain.Money;
import com.example.bookstore.ordering.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderJpaRepository jpa;

    public OrderRepositoryAdapter(SpringDataOrderJpaRepository jpa) { this.jpa = jpa; }

    @Override
    public Order save(Order order) {
        OrderJpaEntity e = toEntity(order);
        e.getLines().forEach(l -> l.setOrder(e));
        return toDomain(jpa.save(e));
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return jpa.findById(id.value()).map(this::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    private OrderJpaEntity toEntity(Order d) {
        var e = new OrderJpaEntity(d.id().value(), d.status().name());
        var lines = d.lines().stream().map(l ->
                new OrderLineJpaEntity(
                        e, l.bookId(), l.title(), l.quantity(),
                        l.unitPrice().amount(), l.unitPrice().currency().getCurrencyCode()
                )
        ).toList();
        e.setLines(lines);
        return e;
    }

    private Order toDomain(OrderJpaEntity e) {
        var lines = e.getLines().stream().map(l ->
                new OrderLine(
                        l.getBookId(), l.getTitle(), l.getQuantity(),
                        new Money(l.getUnitAmount(), Currency.getInstance(l.getCurrency()))
                )
        ).toList();
        var d = new Order(new OrderId(e.getId()), lines); // use private ctor via reflection
        try {
            var statusField = Order.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(d, OrderStatus.valueOf(e.getStatus()));
        } catch (Exception ex) { throw new RuntimeException(ex); }
        return d;
    }
}