package com.example.bookstore.catalog.api;

import com.example.bookstore.catalog.api.dto.BookResponse;
import com.example.bookstore.catalog.api.dto.CreateBookRequest;
import com.example.bookstore.catalog.application.BookService;
import com.example.bookstore.catalog.domain.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Catalog", description = "Manage books in the catalog")
@RestController
@RequestMapping("/api/catalog/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @Operation(summary = "Create a book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBookRequest req) {
        Book saved = service.register(
            req.title, req.author, req.isbn, req.price.amount, req.price.currency, req.stock
        );
        BookResponse body = toResponse(saved);
        return ResponseEntity.created(URI.create("/api/catalog/books/" + body.id)).body(body);
    }

    @Operation(summary = "Get book by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable String id) {
        return service.getById(id)
            .map(b -> ResponseEntity.ok(toResponse(b)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "List all books")
    @GetMapping
    public List<BookResponse> list() {
        return service.listAll().stream().map(this::toResponse).toList();
    }

    private BookResponse toResponse(Book b) {
        BookResponse r = new BookResponse();
        r.id = b.id().toString();
        r.title = b.title();
        r.author = b.author();
        r.isbn = b.isbn().value();
        r.amount = b.price().amount().toPlainString();
        r.currency = b.price().currency().getCurrencyCode();
        r.stock = b.stock();
        return r;
    }
}
