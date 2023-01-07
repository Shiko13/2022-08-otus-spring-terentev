package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.converter.AuthorDtoConverter;
import ru.otus.spring.dto.converter.BookDtoConverter;
import ru.otus.spring.dto.converter.CommentDtoConverter;
import ru.otus.spring.dto.converter.GenreDtoConverter;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = BookController.class)
@TestPropertySource(properties = "mongock.enabled=false")
@Import({BookDtoConverter.class, AuthorDtoConverter.class, CommentDtoConverter.class, GenreDtoConverter.class})
class BookControllerTest {

    private static final Author EXPECTED_AUTHOR = new Author("1", "John Tolkien");
    private static final Author EXPECTED_AUTHOR2 = new Author("2", "Lev Tolstoy");
    private static final AuthorDto EXPECTED_AUTHOR_DTO = new AuthorDto(EXPECTED_AUTHOR.getId(),
            EXPECTED_AUTHOR.getName());
    private static final AuthorDto EXPECTED_AUTHOR_DTO2 = new AuthorDto(EXPECTED_AUTHOR2.getId(),
            EXPECTED_AUTHOR2.getName());
    private static final Genre EXPECTED_GENRE = new Genre("3", "Novella");
    private static final Genre EXPECTED_GENRE2 = new Genre("4", "Fantasy");
    private static final GenreDto EXPECTED_GENRE_DTO = new GenreDto(EXPECTED_GENRE.getId(),
            EXPECTED_GENRE.getName());
    private static final GenreDto EXPECTED_GENRE_DTO2 = new GenreDto(EXPECTED_GENRE2.getId(),
            EXPECTED_GENRE2.getName());
    private static final Book EXPECTED_BOOK = new Book("5", "The Silmarillion",
            EXPECTED_AUTHOR, EXPECTED_GENRE2, List.of(new Comment("6", "Amazing book!"),
            new Comment("7", "Is it a book about big number?")));
    private static final Book EXPECTED_BOOK2 = new Book("8", "The Kreutzer Sonata",
            EXPECTED_AUTHOR2, EXPECTED_GENRE, List.of(new Comment("9", "Is it book about music?")));
    private static final BookDto EXPECTED_BOOK_DTO = new BookDto(EXPECTED_BOOK.getId(),
            EXPECTED_BOOK.getTitle(), EXPECTED_AUTHOR_DTO, EXPECTED_GENRE_DTO2);
    private static final BookDto EXPECTED_BOOK_DTO2 = new BookDto(EXPECTED_BOOK2.getId(),
            EXPECTED_BOOK2.getTitle(), EXPECTED_AUTHOR_DTO2, EXPECTED_GENRE_DTO);

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        when(bookRepository.findById(EXPECTED_BOOK.getId())).thenReturn(Mono.just(EXPECTED_BOOK));
        when(bookRepository.deleteById(EXPECTED_BOOK.getId())).thenReturn(Mono.empty());
        when(commentRepository.deleteAllById(anyList())).thenReturn(Mono.empty());
        when(bookRepository.findAll()).thenReturn(Flux.just(EXPECTED_BOOK, EXPECTED_BOOK2));
    }

    @Test
    void shouldReturnExpectedBooks() {
        List<BookDto> expected = List.of(EXPECTED_BOOK_DTO, EXPECTED_BOOK_DTO2);

        webClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(expected.size())
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(expected));
    }

    @Test
    void shouldReturnExpectedBookById() {
        BookDto expected = EXPECTED_BOOK_DTO;

        webClient.get()
                .uri(String.format("/api/books/%s", expected.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(expected));
    }

    @Test
    void shouldThrowIfBookNotFoundById() {
        when(bookRepository.findById("999")).thenReturn(Mono.empty());

        webClient.get()
                .uri("/api/books/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldPerformDeleteBook() {
        String expectedBookId = EXPECTED_BOOK_DTO.getId();

        webClient.delete()
                .uri(String.format("/api/books/%s", expectedBookId))
                .exchange()
                .expectStatus().isOk();

        verify(bookRepository, times(1)).deleteById(eq(expectedBookId));
        verify(commentRepository, times(1))
                .deleteAllById(eq(EXPECTED_BOOK.getComments()
                        .stream().map(Comment::getId)
                        .collect(Collectors.toList())));
    }

    @Test
    void shouldPerformEditBook() {
        Book bookToSend = new Book(EXPECTED_BOOK.getId(), EXPECTED_BOOK.getTitle(),
                new Author(EXPECTED_AUTHOR.getId(), null),
                new Genre(EXPECTED_GENRE.getId(), null), null);
        BookDto bookToSendDto = new BookDto(EXPECTED_BOOK.getId(), EXPECTED_BOOK.getTitle(),
                new AuthorDto(EXPECTED_AUTHOR_DTO.getId(), null),
                new GenreDto(EXPECTED_GENRE_DTO.getId(), null));
        Book bookToReceive = new Book(EXPECTED_BOOK.getId(), EXPECTED_BOOK.getTitle(),
                EXPECTED_AUTHOR, EXPECTED_GENRE, null);
        BookDto bookToReceiveDto = new BookDto(EXPECTED_BOOK.getId(), EXPECTED_BOOK.getTitle(),
                EXPECTED_AUTHOR_DTO, EXPECTED_GENRE_DTO);
        when(bookRepository.updateBookWithoutComments(any())).thenReturn(Mono.just(bookToReceive));

        webClient.put()
                .uri(String.format("/api/books/%s", bookToSendDto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookToSendDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(bookToReceiveDto));

        verify(bookRepository).updateBookWithoutComments(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(bookToSend);
            return true;
        }));
    }

    @Test
    void shouldPerformCreateBook() {
        Book bookToSend = new Book(null, EXPECTED_BOOK.getTitle(),
                new Author(EXPECTED_AUTHOR.getId(), null),
                new Genre(EXPECTED_GENRE.getId(), null), null);
        BookDto bookToSendDto = new BookDto(null, EXPECTED_BOOK.getTitle(),
                new AuthorDto(EXPECTED_AUTHOR_DTO.getId(), null),
                new GenreDto(EXPECTED_GENRE_DTO.getId(), null));
        Book bookToReceive = new Book("999", EXPECTED_BOOK.getTitle(),
                EXPECTED_AUTHOR, EXPECTED_GENRE, null);
        BookDto bookToReceiveDto = new BookDto("999", EXPECTED_BOOK.getTitle(),
                EXPECTED_AUTHOR_DTO, EXPECTED_GENRE_DTO);
        when(bookRepository.save(any())).thenReturn(Mono.just(bookToReceive));

        webClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookToSendDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .value(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(bookToReceiveDto));

        verify(bookRepository).save(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(bookToSend);
            return true;
        }));
    }
}