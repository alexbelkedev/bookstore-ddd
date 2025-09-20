package com.example.bookstore.ordering.application;

import java.util.Currency;

public interface PaymentsPort {
    enum Result { AUTHORIZED, DECLINED }
    Result authorize(String orderId, String amount, Currency currency, String idempotencyKey);
}