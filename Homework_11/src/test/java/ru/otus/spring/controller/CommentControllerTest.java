package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.converter.AuthorDtoConverter;
import ru.otus.spring.dto.converter.BookDtoConverter;
import ru.otus.spring.dto.converter.CommentDtoConverter;
import ru.otus.spring.dto.converter.GenreDtoConverter;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = CommentController.class)
@TestPropertySource(properties = "mongock.enabled=false")
@Import({BookDtoConverter.class, AuthorDtoConverter.class, CommentDtoConverter.class, GenreDtoConverter.class})
class CommentControllerTest {


    private static final Comment EXPECTED_COMMENT = new Comment("1", "Amazing book!");
    private static final CommentDto EXPECTED_COMMENT_DTO = new CommentDto(EXPECTED_COMMENT.getId(),
            EXPECTED_COMMENT.getText());
    private static final Comment EXPECTED_COMMENT_2 = new Comment("2", "Is it a book about big number?");
    private static final CommentDto EXPECTED_COMMENT_DTO_2 = new CommentDto(EXPECTED_COMMENT_2.getId(),
            EXPECTED_COMMENT_2.getText());
    private static final Book EXPECTED_BOOK = new Book("1", null, null, null,
            List.of(EXPECTED_COMMENT, EXPECTED_COMMENT_2));

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        when(bookRepository.findById(EXPECTED_BOOK.getId())).thenReturn(Mono.just(EXPECTED_BOOK));
        when(commentRepository.findById(EXPECTED_COMMENT.getId())).thenReturn(Mono.just(EXPECTED_COMMENT));
        when(bookRepository.save(any())).thenReturn(Mono.just(EXPECTED_BOOK));
        when(commentRepository.deleteById(EXPECTED_COMMENT.getId())).thenReturn(Mono.empty());
        when(bookRepository.deleteCommentByIdAndBookId(EXPECTED_COMMENT.getId(), EXPECTED_BOOK.getId()))
                .thenReturn(Mono.empty());
    }

    @Test
    void shouldReturnExpectedComments() {
        String bookId = EXPECTED_BOOK.getId();
        List<CommentDto> expected = List.of(EXPECTED_COMMENT_DTO, EXPECTED_COMMENT_DTO_2);

        webClient.get()
                .uri(String.format("/api/books/%s/comments", bookId))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(expected.size())
                .value(actual -> {
                    assertThat(actual)
                            .usingRecursiveComparison()
                            .isEqualTo(expected);
                });
    }

    @Test
    void shouldReturnExpectedCommentById() {
        CommentDto expected = EXPECTED_COMMENT_DTO;

        webClient.get()
                .uri(String.format("/api/books/%s/comments/%s", EXPECTED_BOOK.getId(), expected.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class)
                .value(actual -> {
                    assertThat(actual)
                            .usingRecursiveComparison()
                            .isEqualTo(expected);
                });
    }

    @Test
    void shouldThrowIfCommentNotFoundById() {
        when(commentRepository.findById("999")).thenReturn(Mono.empty());

        webClient.get()
                .uri("/api/books/1/comments/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldPerformDeleteComment() {
        String bookId = EXPECTED_BOOK.getId();
        String commentId = EXPECTED_COMMENT.getId();

        webClient.delete()
                .uri(String.format("/api/books/%s/comments/%s", bookId, commentId))
                .exchange()
                .expectStatus().isOk();

        verify(bookRepository, times(1)).deleteCommentByIdAndBookId(eq(commentId), eq(bookId));
        verify(commentRepository, times(1)).deleteById(eq(commentId));
    }

    @Test
    void shouldPerformEditComment() {
        Comment commentToSend = new Comment(EXPECTED_COMMENT.getId(), UUID.randomUUID().toString());
        CommentDto commentToSendDto = new CommentDto(commentToSend.getId(), commentToSend.getText());
        Comment commentToReceive = new Comment(commentToSend.getId(), commentToSend.getText());
        CommentDto commentToReceiveDto = new CommentDto(commentToSend.getId(), commentToSend.getText());
        when(commentRepository.save(any())).thenReturn(Mono.just(commentToReceive));

        webClient.put()
                .uri(String.format("/api/books/%s/comments/%s", EXPECTED_BOOK.getId(), commentToSend.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(commentToSendDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class)
                .value(actual -> {
                    assertThat(actual)
                            .usingRecursiveComparison()
                            .isEqualTo(commentToReceiveDto);
                });

        verify(commentRepository).save(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(commentToSend);
            return true;
        }));
    }

    @Test
    void shouldPerformCreateComment() {
        Book book = new Book("1", null, null, null,
                new ArrayList<>(List.of(EXPECTED_COMMENT, EXPECTED_COMMENT_2)));
        String bookId = book.getId();
        Comment commentToSend = new Comment(null, UUID.randomUUID().toString());
        CommentDto commentToSendDto = new CommentDto(null, commentToSend.getText());
        Comment commentToReceive = new Comment("999", commentToSend.getText());
        CommentDto commentToReceiveDto = new CommentDto(commentToReceive.getId(), commentToReceive.getText());
        Book expectedBook = new Book("1", null, null, null,
                List.of(EXPECTED_COMMENT, EXPECTED_COMMENT_2, commentToReceive));
        when(commentRepository.save(any())).thenReturn(Mono.just(commentToReceive));
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(book));

        webClient.post()
                .uri(String.format("/api/books/%s/comments", bookId))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(commentToSendDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentDto.class)
                .value(actual -> {
                    assertThat(actual)
                            .usingRecursiveComparison()
                            .isEqualTo(commentToReceiveDto);
                });

        verify(commentRepository).save(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(commentToSend);
            return true;
        }));
        verify(bookRepository).findById(argThat((ArgumentMatcher<String>) s -> {
            assertThat(s).isEqualTo(bookId);
            return true;
        }));
        verify(bookRepository).save(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook);
            return true;
        }));
    }
}
