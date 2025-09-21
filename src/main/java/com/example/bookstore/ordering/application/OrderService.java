package com.example.bookstore.ordering.application;

import com.example.bookstore.common.events.ordering.OrderPlacedPayload;
import com.example.bookstore.common.outbox.OutboxService;
import com.example.bookstore.ordering.domain.Order;
import com.example.bookstore.ordering.domain.OrderId;
import com.example.bookstore.ordering.domain.OrderLine;
import com.example.bookstore.ordering.domain.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class OrderService {

    private final OrderRepository orders;
    private final OutboxService outbox;

    public OrderService(OrderRepository orders, OutboxService outbox) {
        this.orders = orders;
        this.outbox = outbox;
    }

    @Transactional
    public Order placeOrder(List<OrderLine> lines) {
        Order order = Order.createNew(lines);
        order = orders.save(order); // status PENDING_PAYMENT

        var total = order.total();
        outbox.append(
                "orders.order-placed.v1",
                order.id().toString(),
                new OrderPlacedPayload(order.id().toString(), total.amount().toPlainString(), total.currency().getCurrencyCode()),
                Map.of("correlationId", order.id().toString(), "causation", "api.placeOrder")
        );

        return order;
    }

    @Transactional(readOnly = true)
    public Optional<Order> byId(String id) {
        return orders.findById(new OrderId(UUID.fromString(id)));
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orders.findAll();
    }

    // used by event handler
    @Transactional
    public void markPaid(String orderId) {
        orders.findById(new OrderId(UUID.fromString(orderId))).ifPresent(o -> {
            o.markPaid();
            orders.save(o);
        });
    }

    @Transactional
    public void markPaymentFailed(String orderId, String reason) {
        orders.findById(new OrderId(UUID.fromString(orderId))).ifPresent(o -> {
            o.markPaymentFailed();
            orders.save(o);
        });
    }
}