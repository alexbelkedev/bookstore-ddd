package com.example.bookstore.ordering.infrastructure.http;

import com.example.bookstore.ordering.application.PaymentsPort;
import com.example.bookstore.ordering.infrastructure.config.PaymentsClientProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class HttpPaymentsAdapterTest {

    static MockWebServer server;

    @BeforeAll
    static void startServer() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void stopServer() throws Exception {
        server.shutdown();
    }

    private HttpPaymentsAdapter newAdapter() {
        var props = new PaymentsClientProperties();
        props.setBaseUrl(server.url("/").toString()); // point adapter to fake server
        return new HttpPaymentsAdapter(props);
    }

    @Test
    void authorize_success() throws Exception {
        // Arrange fake HTTP response from Payments API
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"authorized\":true,\"paymentId\":\"pay_123\"}"));

        var adapter = newAdapter();

        // Act
        var result = adapter.authorize("order-42", "10.00", Currency.getInstance("EUR"), "idem-xyz");

        // Assert client result
        assertEquals(PaymentsPort.Result.AUTHORIZED, result);

        // Assert HTTP request details
        RecordedRequest req = server.takeRequest();
        assertEquals("/api/payments/authorize", req.getPath());
        assertEquals("POST", req.getMethod());
        String body = req.getBody().readString(StandardCharsets.UTF_8);
        // loose check
        assertTrue(body.contains("\"orderId\":\"order-42\""));
        assertTrue(body.contains("\"amount\":\"10.00\""));
        assertTrue(body.contains("\"currency\":\"EUR\""));
        assertTrue(body.contains("\"idempotencyKey\":\"idem-xyz\""));
    }

    @Test
    void authorize_declined() {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"authorized\":false,\"paymentId\":\"pay_999\"}"));

        var adapter = newAdapter();

        var result = adapter.authorize("order-1", "1200.00", Currency.getInstance("EUR"), "idem-1");

        assertEquals(PaymentsPort.Result.DECLINED, result);
    }

    @Test
    void http_error_bubbles_up() {
        server.enqueue(new MockResponse().setResponseCode(500).setBody("{\"error\":\"boom\"}"));
        var adapter = newAdapter();

        var ex = assertThrows(RuntimeException.class, () ->
                adapter.authorize("order-1", "10.00", Currency.getInstance("EUR"), "idem-err"));
        // Spring's RestClient throws a runtime exception on 5xx; this verifies we really used HTTP.
        assertNotNull(ex.getMessage());
    }
}