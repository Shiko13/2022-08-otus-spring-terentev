package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.converter.CommentDtoConverter;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final CommentDtoConverter commentDtoConverter;

    @GetMapping("/api/books/{bookId}/comments")
    public Flux<CommentDto> listComments(@PathVariable("bookId") String bookId) {
        return bookRepository.findById(bookId)
                .flatMapIterable(b -> (b.getComments() == null ? List.of() : b.getComments()))
                .map(commentDtoConverter::toDto);
    }

    @GetMapping("/api/books/{bookId}/comments/{id}")
    public Mono<ResponseEntity<CommentDto>> getCommentById(@PathVariable("bookId") String bookId,
                                                           @PathVariable("id") String id) {
        return commentRepository.findById(id)
                .map(commentDtoConverter::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books/{bookId}/comments")
    public Mono<ResponseEntity<CommentDto>> createComment(@PathVariable("bookId") String bookId,
                                                    @RequestBody CommentDto comment) {
        return bookRepository.findById(bookId)
                .flatMap(b ->
                        Mono.fromCallable(() -> commentDtoConverter.fromDto(comment))
                                .flatMap(commentRepository::save)
                                .flatMap(c -> {
                                    if (b.getComments() == null) {
                                        b.setComments(new ArrayList<>());
                                    }
                                    b.getComments().add(c);
                                    return bookRepository.save(b).thenReturn(c);
                                })
                )
                .map(commentDtoConverter::toDto)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PutMapping("/api/books/{bookId}/comments/{id}")
    public Mono<ResponseEntity<CommentDto>> editComment(@PathVariable("bookId") String bookId,
                                                        @PathVariable("id") String id,
                                                        @RequestBody CommentDto comment) {
        return Mono.fromCallable(() -> commentDtoConverter.fromDto(comment.setId(id)))
                .flatMap(commentRepository::save)
                .map(commentDtoConverter::toDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/api/books/{bookId}/comments/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable("bookId") String bookId,
                                                    @PathVariable("id") String id) {
        return commentRepository.deleteById(id)
                .then(bookRepository.deleteCommentByIdAndBookId(id, bookId))
                .thenReturn(ResponseEntity.ok().build());
    }
}