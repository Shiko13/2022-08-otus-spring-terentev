package ru.otus.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.BookShortDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.CommentNotFoundException;
import ru.otus.spring.service.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private static final Book EXPECTED_BOOK = new Book(1L, null, null, null, null);
    private static final BookShortDto EXPECTED_BOOK_DTO = new BookShortDto(EXPECTED_BOOK.getId());
    private static final Comment EXPECTED_COMMENT = new Comment(1L, "Amazing book!", EXPECTED_BOOK);
    private static final CommentDto EXPECTED_COMMENT_DTO = new CommentDto(EXPECTED_COMMENT.getId(),
            EXPECTED_COMMENT.getText(), EXPECTED_BOOK_DTO);
    private static final Comment EXPECTED_COMMENT_2 = new Comment(2L, "Is it a book about big number?",
            EXPECTED_BOOK);
    private static final CommentDto EXPECTED_COMMENT_DTO_2 = new CommentDto(EXPECTED_COMMENT_2.getId(),
            EXPECTED_COMMENT_2.getText(), EXPECTED_BOOK_DTO);
    private static final String COMMENT_NOT_FOUND = "Comment 999 not found";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        when(commentService.getById(EXPECTED_COMMENT.getId())).thenReturn(Optional.of(EXPECTED_COMMENT_DTO));
        when(commentService.getByBookId(EXPECTED_BOOK.getId())).thenReturn(List.of(EXPECTED_COMMENT_DTO, EXPECTED_COMMENT_DTO_2));
    }

    @Test
    void shouldReturnExpectedComments() throws Exception {
        long bookId = EXPECTED_BOOK.getId();
        List<CommentDto> expected = List.of(EXPECTED_COMMENT_DTO, EXPECTED_COMMENT_DTO_2);

        mvc.perform(get(String.format("/api/books/%s/comments", bookId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldReturnExpectedCommentById() throws Exception {
        long bookId = EXPECTED_BOOK.getId();
        CommentDto expected = EXPECTED_COMMENT_DTO;

        mvc.perform(get(String.format("/api/books/%s/comments/%s", bookId, expected.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldThrowIfCommentNotFoundById() throws Exception {
        long bookId = EXPECTED_BOOK.getId();
        long commentId = 999;

        mvc.perform(get(String.format("/api/books/%s/comments/%s", bookId, commentId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(COMMENT_NOT_FOUND));
    }

    @Test
    void shouldPerformDeleteComment() throws Exception {
        long bookId = EXPECTED_BOOK.getId();
        long commentId = EXPECTED_COMMENT.getId();

        mvc.perform(delete(String.format("/api/books/%s/comments/%s", bookId, commentId)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteById(commentId);
    }

    @Test
    void shouldReturnErrorIfEditedCommentNotExists() throws Exception {
        long bookId = EXPECTED_BOOK.getId();
        long commentId = 999;
        when(commentService.update(any())).thenThrow(new CommentNotFoundException(commentId));

        CommentDto commentToSend = new CommentDto(EXPECTED_COMMENT.getId(), UUID.randomUUID().toString(),
                EXPECTED_BOOK_DTO);

        mvc.perform(put(String.format("/api/books/%s/comments/%s", bookId, commentToSend.getId()))
                        .content(mapper.writeValueAsString(commentToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(COMMENT_NOT_FOUND));
    }

    @Test
    void shouldPerformEditComment() throws Exception {
        long bookId = EXPECTED_BOOK.getId();

        CommentDto commentToSend = new CommentDto(EXPECTED_COMMENT.getId(), UUID.randomUUID().toString(),
                EXPECTED_BOOK_DTO);
        CommentDto commentExpected = new CommentDto(commentToSend.getId(), commentToSend.getText(),
                EXPECTED_BOOK_DTO);

        when(commentService.update(any())).thenReturn(commentExpected);

        mvc.perform(put(String.format("/api/books/%s/comments/%s", bookId, commentToSend.getId()))
                        .content(mapper.writeValueAsString(commentToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentExpected)));

        verify(commentService).update(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(commentToSend);
            return true;
        }));
    }

    @Test
    void shouldPerformCreateComment() throws Exception {
        long bookId = EXPECTED_BOOK.getId();

        CommentDto commentToSend = new CommentDto(null, UUID.randomUUID().toString(), EXPECTED_BOOK_DTO);
        CommentDto commentExpected = new CommentDto(10000L, commentToSend.getText(), EXPECTED_BOOK_DTO);

        when(commentService.insert(any())).thenReturn(commentExpected);

        mvc.perform(post(String.format("/api/books/%s/comments", bookId))
                        .content(mapper.writeValueAsString(commentToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(commentExpected)));

        verify(commentService).insert(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(commentToSend);
            return true;
        }));
    }
}
