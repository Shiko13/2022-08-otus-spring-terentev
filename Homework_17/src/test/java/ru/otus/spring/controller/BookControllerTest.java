package ru.otus.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.exception.BookNotFoundException;
import ru.otus.spring.service.BookService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final Author EXPECTED_AUTHOR = new Author(1L, "John Tolkien");
    private static final Author EXPECTED_AUTHOR2 = new Author(2L, "Lev Tolstoy");
    private static final AuthorDto EXPECTED_AUTHOR_DTO = new AuthorDto(EXPECTED_AUTHOR.getId(),
            EXPECTED_AUTHOR.getName());
    private static final AuthorDto EXPECTED_AUTHOR_DTO2 = new AuthorDto(EXPECTED_AUTHOR2.getId(),
            EXPECTED_AUTHOR2.getName());
    private static final Genre EXPECTED_GENRE = new Genre(1L, "Novel");
    private static final Genre EXPECTED_GENRE2 = new Genre(2L, "Novella");
    private static final Genre EXPECTED_GENRE3 = new Genre(3L, "Fantasy");
    private static final GenreDto EXPECTED_GENRE_DTO = new GenreDto(EXPECTED_GENRE.getId(),
            EXPECTED_GENRE.getName());
    private static final GenreDto EXPECTED_GENRE_DTO2 = new GenreDto(EXPECTED_GENRE2.getId(),
            EXPECTED_GENRE2.getName());
    private static final GenreDto EXPECTED_GENRE_DTO3 = new GenreDto(EXPECTED_GENRE3.getId(),
            EXPECTED_GENRE3.getName());
    private static final Book EXPECTED_BOOK = new Book(1L, "The Silmarillion",
            EXPECTED_AUTHOR, EXPECTED_GENRE3, null);
    private static final Book EXPECTED_BOOK2 = new Book(2L, "The Kreutzer Sonata",
            EXPECTED_AUTHOR2, EXPECTED_GENRE2, null);
    private static final BookDto EXPECTED_BOOK_DTO = new BookDto(EXPECTED_BOOK.getId(),
            EXPECTED_BOOK.getTitle(), EXPECTED_AUTHOR_DTO, EXPECTED_GENRE_DTO3);
    private static final BookDto EXPECTED_BOOK_DTO2 = new BookDto(EXPECTED_BOOK2.getId(),
            EXPECTED_BOOK2.getTitle(), EXPECTED_AUTHOR_DTO2, EXPECTED_GENRE_DTO2);
    private static final String BOOK_NOT_FOUND = "Book 999 not found";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        when(bookService.getById(EXPECTED_BOOK.getId())).thenReturn(Optional.of(EXPECTED_BOOK_DTO));
        when(bookService.getAll()).thenReturn(List.of(EXPECTED_BOOK_DTO, EXPECTED_BOOK_DTO2));
    }

    @Test
    void shouldReturnExpectedBooks() throws Exception {
        List<BookDto> expected = List.of(EXPECTED_BOOK_DTO, EXPECTED_BOOK_DTO2);

        mvc.perform(get("/api/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldReturnExpectedBookById() throws Exception {
        BookDto expected = EXPECTED_BOOK_DTO;

        mvc.perform(get(String.format("/api/books/%s", expected.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldThrowIfBookNotFoundById() throws Exception {
        long id = 999;

        mvc.perform(get(String.format("/api/books/%s", id)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(BOOK_NOT_FOUND));
    }

    @Test
    void shouldPerformDeleteBook() throws Exception {
        long expectedBookId = EXPECTED_BOOK_DTO.getId();

        mvc.perform(delete(String.format("/api/books/%s", expectedBookId)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteById(expectedBookId);
    }

    @Test
    void shouldReturnErrorIfEditedBookNotExists() throws Exception {
        long id = 999;
        when(bookService.update(any())).thenThrow(new BookNotFoundException(id));
        BookDto bookToSend = new BookDto(id, "TEST", null, null);

        mvc.perform(put(String.format("/api/books/%s", id))
                        .content(mapper.writeValueAsString(bookToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(BOOK_NOT_FOUND));
    }

    @Test
    void shouldPerformEditBook() throws Exception {
        BookDto bookToSend = new BookDto(EXPECTED_BOOK.getId(), EXPECTED_BOOK.getTitle(),
                new AuthorDto(EXPECTED_AUTHOR_DTO.getId(), null),
                new GenreDto(EXPECTED_GENRE_DTO.getId(), null));
        BookDto bookToReceive = new BookDto(EXPECTED_BOOK.getId(), EXPECTED_BOOK.getTitle(), EXPECTED_AUTHOR_DTO,
                EXPECTED_GENRE_DTO);
        when(bookService.update(any())).thenReturn(bookToReceive);

        mvc.perform(put(String.format("/api/books/%s", bookToSend.getId()))
                        .content(mapper.writeValueAsString(bookToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookToReceive)));

        verify(bookService).update(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(bookToSend);
            return true;
        }));
    }

    @Test
    void shouldPerformCreateBook() throws Exception {
        BookDto bookToSend = new BookDto(null, EXPECTED_BOOK.getTitle(),
                new AuthorDto(EXPECTED_AUTHOR_DTO.getId(), null),
                new GenreDto(EXPECTED_GENRE_DTO.getId(), null));
        BookDto bookExpected = new BookDto(1000L, EXPECTED_BOOK.getTitle(), EXPECTED_AUTHOR_DTO,
                EXPECTED_GENRE_DTO);
        when(bookService.insert(any())).thenReturn(bookExpected);

        mvc.perform(post("/api/books")
                        .content(mapper.writeValueAsString(bookToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookExpected)));

        verify(bookService).insert(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(bookToSend);
            return true;
        }));
    }
}