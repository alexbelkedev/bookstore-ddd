package com.example.bookstore.payments.api;

import com.example.bookstore.payments.application.PaymentsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Payments", description = "Payment operations")
@RestController
@RequestMapping("/api/payments")
public class PaymentsController {
    private final PaymentsFacade facade;

    public PaymentsController(PaymentsFacade facade) { this.facade = facade; }

    @Operation(summary = "Authorize payment")
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