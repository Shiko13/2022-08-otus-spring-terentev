package ru.otus.spring.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.exception.BookNotFoundException;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMetrics
@AutoConfigureWebTestClient
@TestPropertySource(properties = "mongock.enabled=false")
class BookControllerTest {

    private static final String MONGODB = "mongoDb";
    private static final String FAILED_WITH_RETRY = "failed_with_retry";
    private static final String SUCCESS_WITHOUT_RETRY = "successful_without_retry";
    private static final Author EXPECTED_AUTHOR = new Author("1", "John Tolkien");
    private static final Author EXPECTED_AUTHOR2 = new Author("2", "Miguel Cervantes");
    private static final AuthorDto EXPECTED_AUTHOR_DTO = new AuthorDto(EXPECTED_AUTHOR.getId(),
            EXPECTED_AUTHOR.getName());
    private static final AuthorDto EXPECTED_AUTHOR_DTO2 = new AuthorDto(EXPECTED_AUTHOR2.getId(),
            EXPECTED_AUTHOR2.getName());
    private static final AuthorDto FALLBACK_AUTHOR_DTO = new AuthorDto("00", "Without name");
    private static final Genre EXPECTED_GENRE = new Genre("3", "Fantasy");
    private static final Genre EXPECTED_GENRE2 = new Genre("4", "Novel");
    private static final GenreDto EXPECTED_GENRE_DTO = new GenreDto(EXPECTED_GENRE.getId(),
            EXPECTED_GENRE.getName());
    private static final GenreDto EXPECTED_GENRE_DTO2 = new GenreDto(EXPECTED_GENRE2.getId(),
            EXPECTED_GENRE2.getName());
    private static final GenreDto FALLBACK_GENRE_DTO = new GenreDto("000", "Without name");
    private static final Book EXPECTED_BOOK = new Book("5", "The Silmarillion", EXPECTED_AUTHOR,
            EXPECTED_GENRE, List.of(new Comment("6", "Amazing book!"),
            new Comment("7", "Is it a book about big number?")));
    private static final Book EXPECTED_BOOK2 = new Book("8", "The Ingenious Gentleman Don Quixote of La Mancha",
            EXPECTED_AUTHOR2, EXPECTED_GENRE2,
            List.of(new Comment("9", "Sancho Panza is my crush ^_^"),
                    new Comment("10", "Dulcinea del Toboso is not princess")));
    private static final BookDto EXPECTED_BOOK_DTO = new BookDto(EXPECTED_BOOK.getId(),
            EXPECTED_BOOK.getTitle(), EXPECTED_AUTHOR_DTO, EXPECTED_GENRE_DTO);
    private static final BookDto EXPECTED_BOOK_DTO2 = new BookDto(EXPECTED_BOOK2.getId(),
            EXPECTED_BOOK2.getTitle(), EXPECTED_AUTHOR_DTO2, EXPECTED_GENRE_DTO2);
    private static final BookDto FALLBACK_BOOK_DTO = new BookDto("0", "Without title",
            FALLBACK_AUTHOR_DTO, FALLBACK_GENRE_DTO);

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private RetryRegistry retryRegistry;

    @BeforeEach
    public void setUp() {
        when(bookRepository.findById(EXPECTED_BOOK.getId())).thenReturn(Mono.just(EXPECTED_BOOK));
        when(bookRepository.deleteById(EXPECTED_BOOK.getId())).thenReturn(Mono.empty());
        when(commentRepository.deleteAllById(anyList())).thenReturn(Mono.empty());
        when(bookRepository.findAll()).thenReturn(Flux.just(EXPECTED_BOOK, EXPECTED_BOOK2));
    }

    @Test
    void shouldFallbackWhenOpenedCircuitBreaker() {
        circuitBreakerRegistry.circuitBreaker(MONGODB).transitionToOpenState();

        checkHealthStatus(CircuitBreaker.State.OPEN);

        webClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(1)
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(FALLBACK_BOOK_DTO)));

    }

    @Test
    void shouldReturnCorrectDataWhenClosedCircuitBreaker() {
        circuitBreakerRegistry.circuitBreaker(MONGODB).transitionToClosedState();

        checkHealthStatus(CircuitBreaker.State.CLOSED);

        webClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(EXPECTED_BOOK_DTO, EXPECTED_BOOK_DTO2)));

    }


    @Test
    void shouldNotRetryIfBookNotFoundException() {
        long failedWithRetryCount = getCurrentCount(FAILED_WITH_RETRY);
        circuitBreakerRegistry.circuitBreaker(MONGODB).transitionToClosedState();

        AtomicInteger retried = new AtomicInteger();
        when(bookRepository.findAll()).thenReturn(
                Mono.<Book>fromCallable(() -> {
                    retried.incrementAndGet();
                    throw new BookNotFoundException("TEST");
                }).flux()
        );

        webClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(1)
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(FALLBACK_BOOK_DTO)));

        assertThat(getCurrentCount(FAILED_WITH_RETRY)).isEqualTo(failedWithRetryCount);

        assertThat(retried.get()).isEqualTo(1);
    }

    @Test
    void shouldNotRetryIfSuccess() {
        long successWithoutRetryCount = getCurrentCount(SUCCESS_WITHOUT_RETRY);
        circuitBreakerRegistry.circuitBreaker(MONGODB).transitionToClosedState();

        webClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(EXPECTED_BOOK_DTO, EXPECTED_BOOK_DTO2)));

        assertThat(getCurrentCount(SUCCESS_WITHOUT_RETRY)).isEqualTo(successWithoutRetryCount + 1);
    }

    private void checkHealthStatus(CircuitBreaker.State state) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(BookControllerTest.MONGODB);
        assertThat(circuitBreaker.getState()).isEqualTo(state);
    }

    private long getCurrentCount(String kind) {
        Retry.Metrics metrics = retryRegistry.retry(BookControllerTest.MONGODB).getMetrics();

        if (FAILED_WITH_RETRY.equals(kind)) {
            return metrics.getNumberOfFailedCallsWithRetryAttempt();
        }
        if (SUCCESS_WITHOUT_RETRY.equals(kind)) {
            return metrics.getNumberOfSuccessfulCallsWithoutRetryAttempt();
        }

        return 0;
    }
}