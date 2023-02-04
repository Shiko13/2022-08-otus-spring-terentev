package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.BookService;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public Flux<BookDto> listBooks() {
        return bookService.listBooks();
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable("id") String id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody BookDto book) {
        return bookService.createBook(book)
                .map(b -> ResponseEntity.status(HttpStatus.CREATED).body(b));
    }

    @PutMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> editBook(@PathVariable("id") String id, @RequestBody BookDto book) {
        return bookService.editBook(id, book)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookService.deleteBook(id)
                .map(ResponseEntity::ok);
    }
}