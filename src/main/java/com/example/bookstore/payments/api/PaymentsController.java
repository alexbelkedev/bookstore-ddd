package com.example.bookstore.payments.api;

import com.example.bookstore.payments.application.PaymentsFacade;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {
    private final PaymentsFacade facade;

    public PaymentsController(PaymentsFacade facade) { this.facade = facade; }

    @PostMapping("/authorize")
    public Map<String, Object> authorize(@RequestBody Map<String, String> body) {
        var r = facade.authorize(
                body.get("orderId"),
                body.get("amount"),
                body.get("currency"),
                body.get("idempotencyKey")
        );
        return Map.of("authorized", r.authorized(), "paymentId", r.paymentId());
    }
}