package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.CommentNotFoundException;
import ru.otus.spring.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/books/{bookId}/comments")
    public ResponseEntity<List<CommentDto>> listComments(@PathVariable("bookId") long bookId) {
        return ResponseEntity.ok(commentService.getByBookId(bookId));
    }

    @GetMapping("/api/books/{bookId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("bookId") long bookId, @PathVariable("id") long id) {
        CommentDto comment = commentService.getById(id).orElseThrow(() -> new CommentNotFoundException(id));
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/api/books/{bookId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("bookId") long bookId,
                                                    @RequestBody CommentDto comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.insert(comment));
    }

    @PutMapping("/api/books/{bookId}/comments/{id}")
    public ResponseEntity<CommentDto> editComment(@PathVariable("bookId") long bookId, @PathVariable("id") long id,
                                                  @RequestBody CommentDto comment) {
        return ResponseEntity.ok(commentService.update(comment));
    }

    @DeleteMapping("/api/books/{bookId}/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("bookId") long bookId, @PathVariable("id") long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(CommentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}