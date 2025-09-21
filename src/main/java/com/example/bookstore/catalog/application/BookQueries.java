package com.example.bookstore.catalog.application;

import java.util.List;

public interface BookQueries {
    record Criteria(String q, int page, int size) {
    }

    record Row(String id, String title, String author, String isbn, String amount, String currency, int stock) {
    }

    record PageResult(List<Row> items, long total, int page, int size) {
    }

    PageResult search(Criteria criteria);
}