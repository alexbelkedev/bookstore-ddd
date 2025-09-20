package com.example.bookstore.ordering.infrastructure.jpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderLineJpaEntity> lines = new ArrayList<>();

    protected OrderJpaEntity() { }

    public OrderJpaEntity(UUID id, String status) {
        this.id = id; this.status = status;
    }

    public UUID getId() { return id; }
    public String getStatus() { return status; }
    public List<OrderLineJpaEntity> getLines() { return lines; }

    public void setId(UUID id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setLines(List<OrderLineJpaEntity> lines) { this.lines = lines; }
}