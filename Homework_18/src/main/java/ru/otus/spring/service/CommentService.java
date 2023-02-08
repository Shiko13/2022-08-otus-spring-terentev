package ru.otus.spring.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.CommentDto;

public interface CommentService {

    Flux<CommentDto> listComments(String bookId);

    Mono<CommentDto> getCommentById(String bookId, String id);

    Mono<CommentDto> createComment(String bookId, CommentDto commentDto);

    Mono<CommentDto> editComment(String bookId, String commentId, CommentDto comment);

    Mono<Void> deleteComment(String bookId, String commentId);
}
