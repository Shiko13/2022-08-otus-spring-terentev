package ru.otus.spring.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final DtoConverter<Book, BookDto> bookConverter;


    @Override
    @Retry(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb", fallbackMethod = "listBooksFallback")
    public Flux<BookDto> listBooks() {
        return bookRepository.findAll()
                .map(bookConverter::toDto)
                .delayElements(Duration.of(5, TimeUnit.SECONDS.toChronoUnit())) // for check
                ;
    }

    @Override
    @Retry(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb", fallbackMethod = "getBookByIdFallback")
    public Mono<BookDto> getBookById(String id) {
        return bookRepository.findById(id)
                .map(bookConverter::toDto);
    }

    @Override
    @Bulkhead(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb")
    public Mono<BookDto> createBook(BookDto book) {
        return Mono.fromCallable(() -> bookConverter.fromDto(book))
                .flatMap(bookRepository::save)
                .map(bookConverter::toDto);
    }

    @Override
    @Bulkhead(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb")
    public Mono<BookDto> editBook(String id, BookDto book) {
        return Mono.fromCallable(() -> bookConverter.fromDto(book.setId(id)))
                .flatMap(bookRepository::updateBookWithoutComments)
                .map(bookConverter::toDto);
    }

    @Override
    @Bulkhead(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb")
    public Mono<Void> deleteBook(String id) {
        return bookRepository.findById(id)
                .flatMap(b -> {
                    List<Comment> comments = (b.getComments() == null) ? List.of() : b.getComments();
                    return commentRepository.deleteAllById(comments.stream()
                                    .map(Comment::getId)
                                    .collect(Collectors.toList()))
                            .then(bookRepository.deleteById(id));
                });
    }

    private Flux<BookDto> listBooksFallback(Throwable t) {
        return Flux.just(new BookDto("0", "Without title", new AuthorDto("00", "Without name"),
                new GenreDto("000", "Without name")));
    }

    private Mono<BookDto> getBookByIdFallback(Throwable t) {
        return Mono.just(new BookDto("0", "Without title", new AuthorDto("00", "Without name"),
                new GenreDto("000", "Without name")));
    }
}