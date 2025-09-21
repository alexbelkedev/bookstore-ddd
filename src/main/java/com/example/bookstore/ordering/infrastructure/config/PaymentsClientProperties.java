package com.example.bookstore.ordering.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ordering.payments")
public class PaymentsClientProperties {
    /**
     * Base URL of the Payments API (e.g. http://localhost:8080).
     */
    private String baseUrl = "http://localhost:8080";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}