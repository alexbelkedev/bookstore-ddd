package com.example.bookstore.catalog.api.dto;

import jakarta.validation.constraints.*;

public class CreateBookRequest {
    @NotBlank public String title;
    @NotBlank public String author;
    @NotBlank @Pattern(regexp = "^[\\d\\-\\s]{10,17}$") public String isbn;

    @NotNull public Price price;
    @Min(0) public int stock = 0;

    public static class Price {
        @NotBlank public String amount;
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") public String currency;
    }
}
