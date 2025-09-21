package com.example.bookstore;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiGroupingConfig {
    @Bean
    GroupedOpenApi catalogApi() {
        return GroupedOpenApi.builder().group("catalog").packagesToScan("com.example.bookstore.catalog").build();
    }

    @Bean
    GroupedOpenApi orderingApi() {
        return GroupedOpenApi.builder().group("ordering").packagesToScan("com.example.bookstore.ordering").build();
    }

    @Bean
    GroupedOpenApi paymentsApi() {
        return GroupedOpenApi.builder().group("payments").packagesToScan("com.example.bookstore.payments").build();
    }
}