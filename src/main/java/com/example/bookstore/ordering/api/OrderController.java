package com.example.bookstore.ordering.api;

import com.example.bookstore.catalog.domain.Money;
import com.example.bookstore.ordering.api.dto.OrderResponse;
import com.example.bookstore.ordering.api.dto.PlaceOrderRequest;
import com.example.bookstore.ordering.application.OrderService;
import com.example.bookstore.ordering.domain.Order;
import com.example.bookstore.ordering.domain.OrderLine;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> place(@Valid @RequestBody PlaceOrderRequest req) {
        List<OrderLine> lines = req.lines.stream().map(l ->
                new OrderLine(
                        UUID.fromString(l.bookId),
                        l.title,
                        l.quantity,
                        Money.of(l.unitAmount, l.currency)
                )
        ).toList();

        Order saved = service.placeOrder(lines);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.id()))
                .body(toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable String id) {
        return service.byId(id)
                .map(o -> ResponseEntity.ok(toResponse(o)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<OrderResponse> list() {
        return service.list().stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(Order o) {
        var r = new OrderResponse();
        r.id = o.id().toString();
        r.status = o.status();
        var total = o.total();
        r.totalAmount = total.amount().toPlainString();
        r.currency = total.currency().getCurrencyCode();
        r.lines = o.lines().stream().map(l -> {
            var rl = new OrderResponse.Line();
            rl.bookId = l.bookId().toString();
            rl.title = l.title();
            rl.quantity = l.quantity();
            rl.unitAmount = l.unitPrice().amount().toPlainString();
            return rl;
        }).toList();
        return r;
    }
}