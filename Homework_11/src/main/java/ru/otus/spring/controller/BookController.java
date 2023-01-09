package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.converter.BookDtoConverter;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final BookDtoConverter bookDtoConverter;

    @GetMapping("/api/books")
    public Flux<BookDto> listBooks() {
        return bookRepository.findAll()
                .map(bookDtoConverter::toDto);
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(bookDtoConverter::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody BookDto book) {
        return Mono.fromCallable(() -> bookDtoConverter.fromDto(book))
                .flatMap(bookRepository::save)
                .map(bookDtoConverter::toDto)
                .map(b -> ResponseEntity.status(HttpStatus.CREATED).body(b));
    }

    @PutMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> editBook(@PathVariable("id") String id, @RequestBody BookDto book) {
        return Mono.fromCallable(() -> bookDtoConverter.fromDto(book.setId(id)))
                .flatMap(bookRepository::updateBookWithoutComments)
                .map(bookDtoConverter::toDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .flatMap(b -> {
                    List<Comment> comments = (b.getComments() == null) ? List.of() : b.getComments();
                    return commentRepository.deleteAllById(comments.stream()
                            .map(Comment::getId)
                            .collect(Collectors.toList()))
                            .then(bookRepository.deleteById(id));
                })
                .map(ResponseEntity::ok);
    }
}