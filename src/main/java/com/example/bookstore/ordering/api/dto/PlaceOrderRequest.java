package com.example.bookstore.ordering.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaceOrderRequest {
    @NotNull public List<Line> lines;

    public static class Line {
        @NotBlank public String bookId;      // UUID as string
        @NotBlank public String title;       // snapshot
        @NotBlank public String currency;    // e.g. "EUR"
        @NotBlank public String unitAmount;  // e.g. "19.99"
        @Min(1) public int quantity;
    }
}