package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.service.CommentService;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/books/{bookId}/comments")
    public Flux<CommentDto> listComments(@PathVariable("bookId") String bookId) {
        return commentService.listComments(bookId);
    }

    @GetMapping("/api/books/{bookId}/comments/{id}")
    public Mono<ResponseEntity<CommentDto>> getCommentById(@PathVariable("bookId") String bookId,
                                                           @PathVariable("id") String id) {
        return commentService.getCommentById(bookId, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books/{bookId}/comments")
    public Mono<ResponseEntity<CommentDto>> createComment(@PathVariable("bookId") String bookId,
                                                    @RequestBody CommentDto comment) {
        return commentService.createComment(bookId, comment)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PutMapping("/api/books/{bookId}/comments/{id}")
    public Mono<ResponseEntity<CommentDto>> editComment(@PathVariable("bookId") String bookId,
                                                        @PathVariable("id") String id,
                                                        @RequestBody CommentDto comment) {
        return commentService.editComment(bookId, id, comment)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/api/books/{bookId}/comments/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable("bookId") String bookId,
                                                    @PathVariable("id") String id) {
        return commentService.deleteComment(bookId, id)
                .thenReturn(ResponseEntity.ok().build());
    }
}