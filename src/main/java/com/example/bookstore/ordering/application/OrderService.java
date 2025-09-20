package com.example.bookstore.ordering.application;

import com.example.bookstore.ordering.domain.Order;
import com.example.bookstore.ordering.domain.OrderId;
import com.example.bookstore.ordering.domain.OrderLine;
import com.example.bookstore.ordering.domain.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderService {
    private final OrderRepository orders;
    private final PaymentsPort payments;

    public OrderService(OrderRepository orders, PaymentsPort payments) {
        this.orders = orders;
        this.payments = payments;
    }

    @Transactional
    public Order placeOrder(List<OrderLine> lines) {
        Order order = Order.createNew(lines);
        order = orders.save(order);

        // Payment authorization (through the port)
        var total = order.total();
        var result = payments.authorize(
                order.id().toString(),
                total.amount().toPlainString(),
                total.currency(),
                "order-" + order.id().toString() // naive idempotency key
        );
        if (result == PaymentsPort.Result.AUTHORIZED) order.markPaid();
        else order.markPaymentFailed();

        return orders.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<Order> byId(String id) {
        return orders.findById(new OrderId(UUID.fromString(id)));
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orders.findAll();
    }
}