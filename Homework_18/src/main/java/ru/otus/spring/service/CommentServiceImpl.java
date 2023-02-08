package ru.otus.spring.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final DtoConverter<Comment, CommentDto> commentConverter;


    @Override
    @Retry(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb", fallbackMethod = "listCommentsFallback")
    public Flux<CommentDto> listComments(String bookId) {
        return bookRepository.findById(bookId)
                .flatMapIterable(book -> (book.getComments() == null) ? List.of() : book.getComments())
                .map(commentConverter::toDto);
    }

    @Override
    @Retry(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb", fallbackMethod = "getCommentByIdFallback")
    public Mono<CommentDto> getCommentById(String bookId, String id) {
        return commentRepository.findById(id)
                .map(commentConverter::toDto);
    }

    @Override
    @Bulkhead(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb")
    public Mono<CommentDto> createComment(String bookId, CommentDto commentDto) {
        return bookRepository.findById(bookId)
                .flatMap(book ->
                        Mono.fromCallable(() -> commentConverter.fromDto(commentDto))
                                .flatMap(commentRepository::save)
                                .flatMap(c -> {
                                    if (book.getComments() == null) {
                                        book.setComments(new ArrayList<>());
                                    }
                                    book.getComments().add(c);
                                    return bookRepository.save(book).thenReturn(c);
                                })
                )
                .map(commentConverter::toDto);
    }

    @Override
    @Bulkhead(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb")
    public Mono<CommentDto> editComment(String bookId, String commentId, CommentDto comment) {
        return Mono.fromCallable(() -> commentConverter.fromDto(comment.setId(commentId)))
                .flatMap(commentRepository::save)
                .map(commentConverter::toDto);
    }

    @Override
    @Bulkhead(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb")
    public Mono<Void> deleteComment(String bookId, String commentId) {
        return commentRepository.deleteById(commentId)
                .then(bookRepository.deleteCommentByIdAndBookId(commentId, bookId));
    }

    private Flux<CommentDto> listCommentsFallback(Throwable t) {
        return Flux.just(new CommentDto("00000", "Without text"));
    }

    private Mono<CommentDto> getCommentByIdFallback(Throwable t) {
        return Mono.just(new CommentDto("00000", "Without text"));
    }
}
