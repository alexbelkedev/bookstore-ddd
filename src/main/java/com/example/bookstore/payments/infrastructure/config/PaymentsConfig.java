package com.example.bookstore.payments.infrastructure.config;

import com.example.bookstore.payments.application.PaymentsFacade;
import com.example.bookstore.payments.domain.PaymentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentsConfig {
    @Bean
    public PaymentsFacade paymentsFacade(PaymentRepository repo) {
        return new PaymentsFacade(repo);
    }
}