package com.example.bookstore.ordering.infrastructure.config;

import com.example.bookstore.ordering.application.OrderService;
import com.example.bookstore.ordering.application.PaymentsPort;
import com.example.bookstore.ordering.domain.OrderRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PaymentsClientProperties.class)
public class OrderingConfig {

    @Bean
    public OrderService orderService(OrderRepository repo, PaymentsPort paymentsPort) {
        return new OrderService(repo, paymentsPort);
    }
}