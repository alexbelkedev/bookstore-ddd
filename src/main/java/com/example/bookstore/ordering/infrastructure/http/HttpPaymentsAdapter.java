package com.example.bookstore.ordering.infrastructure.http;

import com.example.bookstore.ordering.application.PaymentsPort;
import com.example.bookstore.ordering.infrastructure.config.PaymentsClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Currency;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ordering.payments.mode", havingValue = "http")
public class HttpPaymentsAdapter implements PaymentsPort {

    private final RestClient client;

    public HttpPaymentsAdapter(PaymentsClientProperties props) {
        this.client = RestClient.builder().baseUrl(props.getBaseUrl()).build();
    }

    @Override
    public Result authorize(String orderId, String amount, Currency currency, String idempotencyKey) {
        var payload = Map.of(
                "orderId", orderId,
                "amount", amount,
                "currency", currency.getCurrencyCode(),
                "idempotencyKey", idempotencyKey
        );
        Map resp = client.post()
                .uri("/api/payments/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(Map.class);

        boolean authorized = Boolean.TRUE.equals(resp.get("authorized"));
        return authorized ? Result.AUTHORIZED : Result.DECLINED;
    }
}