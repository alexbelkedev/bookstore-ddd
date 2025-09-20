package com.example.bookstore.ordering.infrastructure.acl;

import com.example.bookstore.ordering.application.PaymentsPort;
import com.example.bookstore.payments.application.PaymentsFacade;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class LocalPaymentsAdapter implements PaymentsPort {

    private final PaymentsFacade payments;

    public LocalPaymentsAdapter(PaymentsFacade payments) {
        this.payments = payments;
    }

    @Override
    public Result authorize(String orderId, String amount, Currency currency, String idempotencyKey) {
        var r = payments.authorize(orderId, amount, currency.getCurrencyCode(), idempotencyKey);
        return r.authorized() ? Result.AUTHORIZED : Result.DECLINED;
    }
}