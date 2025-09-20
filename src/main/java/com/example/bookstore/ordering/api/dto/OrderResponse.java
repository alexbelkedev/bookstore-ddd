package com.example.bookstore.ordering.api.dto;

import com.example.bookstore.ordering.domain.OrderStatus;

import java.util.List;

public class OrderResponse {
    public String id;
    public OrderStatus status;
    public String totalAmount;
    public String currency;
    public List<Line> lines;

    public static class Line {
        public String bookId;
        public String title;
        public int quantity;
        public String unitAmount;
    }
}