package com.example.bookstore.ordering.infrastructure.jpa;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
public class OrderLineJpaEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;

    @Column(nullable = false) private UUID bookId;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private int quantity;

    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal unitAmount;
    @Column(nullable = false, length = 3) private String currency;

    protected OrderLineJpaEntity() { }

    public OrderLineJpaEntity(OrderJpaEntity order, UUID bookId, String title, int quantity,
                              BigDecimal unitAmount, String currency) {
        this.order = order; this.bookId = bookId; this.title = title; this.quantity = quantity;
        this.unitAmount = unitAmount; this.currency = currency;
    }

    public Long getId(){ return id; }
    public OrderJpaEntity getOrder(){ return order; }
    public UUID getBookId(){ return bookId; }
    public String getTitle(){ return title; }
    public int getQuantity(){ return quantity; }
    public BigDecimal getUnitAmount(){ return unitAmount; }
    public String getCurrency(){ return currency; }

    public void setOrder(OrderJpaEntity order){ this.order = order; }
}