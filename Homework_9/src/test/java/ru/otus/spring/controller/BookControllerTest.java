package ru.otus.spring.controller;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    private static final AuthorDto EXPECTED_AUTHOR_1 = AuthorDto.builder()
            .id(1L)
            .name("John")
            .surname("Tolkien")
            .build();

    private static final AuthorDto EXPECTED_AUTHOR_2 = AuthorDto.builder()
            .id(2L)
            .name("Lev")
            .surname("Tolstoy")
            .build();

    private static final AuthorDto EXPECTED_AUTHOR_3 = AuthorDto.builder()
            .id(3L)
            .name("Miguel")
            .surname("Cervantes")
            .build();

    private static final GenreDto EXPECTED_GENRE_1 = GenreDto.builder()
            .id(1L)
            .name("Novel")
            .build();

    private static final GenreDto EXPECTED_GENRE_2 = GenreDto.builder()
            .id(2L)
            .name("Novella")
            .build();

    private static final GenreDto EXPECTED_GENRE_3 = GenreDto.builder()
            .id(3L)
            .name("Fantasy")
            .build();
    private static final BookDto EXPECTED_BOOK_1 = new BookDto(1L, "The Silmarillion",
            1977, EXPECTED_AUTHOR_1, EXPECTED_GENRE_3);

    private static final BookDto EXPECTED_BOOK_2 = new BookDto(2L, "The Kreutzer Sonata",
            1889, EXPECTED_AUTHOR_2, EXPECTED_GENRE_2);

    private static final BookDto EXPECTED_BOOK_3 = new BookDto(3L,"The Ingenious Gentleman Don Quixote of La Mancha",
            1605, EXPECTED_AUTHOR_3, EXPECTED_GENRE_1);


    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @BeforeEach
    public void setUp() {
        when(bookService.getById(EXPECTED_BOOK_1.getId())).thenReturn(Optional.of(EXPECTED_BOOK_1));
        when(bookService.getById(EXPECTED_BOOK_2.getId())).thenReturn(Optional.of(EXPECTED_BOOK_2));
        when(bookService.getById(EXPECTED_BOOK_3.getId())).thenReturn(Optional.of(EXPECTED_BOOK_3));
        when(bookService.getAll()).thenReturn(List.of(EXPECTED_BOOK_1, EXPECTED_BOOK_2, EXPECTED_BOOK_3));
        when(authorService.getAll()).thenReturn(List.of(EXPECTED_AUTHOR_1, EXPECTED_AUTHOR_2, EXPECTED_AUTHOR_3));
        when(genreService.getAll()).thenReturn(List.of(EXPECTED_GENRE_1, EXPECTED_GENRE_2, EXPECTED_GENRE_3));
    }

    @Test
    void shouldReturnExpectedBooks() throws Exception {

        List<BookDto> expected = List.of(EXPECTED_BOOK_1, EXPECTED_BOOK_2, EXPECTED_BOOK_3);

        mvc.perform(get("/book"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", new BookListMatcher(expected)))
                .andExpect(view().name("book/list"));
    }

    @Test
    void shouldPerformDeleteBook() throws Exception {
        long expectedBookId = EXPECTED_BOOK_1.getId();

        mvc.perform(post("/book/remove")
                        .param("id", String.valueOf(expectedBookId))
                )
                .andDo(print())
                .andExpect(redirectedUrl("/book"));
    }

    @Test
    void shouldReturnDataForEditBook() throws Exception {
        long expectedBookId = EXPECTED_BOOK_1.getId();

        mvc.perform(get("/book/edit")
                        .queryParam("id", Long.toString(expectedBookId))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", new BookMatcher(EXPECTED_BOOK_1)))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", new AuthorListMatcher(List.of(EXPECTED_AUTHOR_1, EXPECTED_AUTHOR_2, EXPECTED_AUTHOR_3))))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", new GenreListMatcher(List.of(EXPECTED_GENRE_1, EXPECTED_GENRE_2, EXPECTED_GENRE_3))))
                .andExpect(view().name("book/edit"));
    }

    @Test
    void shouldPerformEditBook() throws Exception {
        BookDto expectedBook = new BookDto(1L, "The Silmarillion",
                1977, new AuthorDto(EXPECTED_AUTHOR_1.getId()), new GenreDto(EXPECTED_GENRE_3.getId()));

        mvc.perform(post("/book/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", Long.toString(expectedBook.getId()))
                        .param("title", expectedBook.getTitle())
                        .param("publicationYear", Integer.toString(expectedBook.getPublicationYear()))
                        .param("author", Long.toString(expectedBook.getAuthor().getId()))
                        .param("genre", Long.toString(expectedBook.getGenre().getId()))
                )
                .andDo(print())
                .andExpect(redirectedUrl("/book"));

        verify(bookService).insert(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook);
            return true;
        }));
    }

    @Test
    void shouldReturnDataForCreateBook() throws Exception {
        mvc.perform(get("/book/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", new BookMatcher(new BookDto(null, "", 2022,
                        new AuthorDto(null, null, null), new GenreDto(null, null)))))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", new AuthorListMatcher(List.of(EXPECTED_AUTHOR_1, EXPECTED_AUTHOR_2, EXPECTED_AUTHOR_3))))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", new GenreListMatcher(List.of(EXPECTED_GENRE_1, EXPECTED_GENRE_2, EXPECTED_GENRE_3))))
                .andExpect(view().name("book/add"));
    }

    @Test
    void shouldPerformCreateBook() throws Exception {
        BookDto expectedBook = new BookDto(null, "Resurrection", 1889,
                new AuthorDto(EXPECTED_AUTHOR_2.getId()), new GenreDto(EXPECTED_GENRE_1.getId()));

        mvc.perform(post("/book/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", expectedBook.getTitle())
                        .param("publicationYear", Integer.toString(expectedBook.getPublicationYear()))
                        .param("author", Long.toString(expectedBook.getAuthor().getId()))
                        .param("genre", Long.toString(expectedBook.getGenre().getId()))
                )
                .andDo(print())
                .andExpect(redirectedUrl("/book"));

        verify(bookService).insert(argThat(actual -> {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook);
            return true;
        }));
    }

    static class BookListMatcher extends AssertionMatcher<List<BookDto>> {

        private final List<BookDto> expected;

        public BookListMatcher(List<BookDto> expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(List<BookDto> actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().build())
                    .containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    static class BookMatcher extends AssertionMatcher<BookDto> {

        private final BookDto expected;

        public BookMatcher(BookDto expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(BookDto actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    static class AuthorListMatcher extends AssertionMatcher<List<AuthorDto>> {

        private final List<AuthorDto> expected;

        public AuthorListMatcher(List<AuthorDto> expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(List<AuthorDto> actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().build())
                    .containsExactlyInAnyOrderElementsOf(expected);
        }
    }


    static class GenreListMatcher extends AssertionMatcher<List<GenreDto>> {

        private final List<GenreDto> expected;

        public GenreListMatcher(List<GenreDto> expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(List<GenreDto> actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().build())
                    .containsExactlyInAnyOrderElementsOf(expected);
        }
    }
}
