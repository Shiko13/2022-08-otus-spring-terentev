package ru.otus.spring.controller;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    private static final Author EXPECTED_AUTHOR_1 = Author.builder()
            .id(1L)
            .name("John")
            .surname("Tolkien")
            .build();

    private static final Author EXPECTED_AUTHOR_2 = Author.builder()
            .id(2L)
            .name("Lev")
            .surname("Tolstoy")
            .build();

    private static final Author EXPECTED_AUTHOR_3 = Author.builder()
            .id(3L)
            .name("Miguel")
            .surname("Cervantes")
            .build();

    private static final Genre EXPECTED_GENRE_1 = Genre.builder()
            .id(1L)
            .name("Novel")
            .build();

    private static final Genre EXPECTED_GENRE_2 = Genre.builder()
            .id(2L)
            .name("Novella")
            .build();

    private static final Genre EXPECTED_GENRE_3 = Genre.builder()
            .id(3L)
            .name("Fantasy")
            .build();
    private static final Book EXPECTED_BOOK_1 = Book.builder()
            .id(1L)
            .title("The Silmarillion")
            .publicationYear(1889)
            .author(EXPECTED_AUTHOR_1)
            .genre(EXPECTED_GENRE_3)
            .build();

    private static final Book EXPECTED_BOOK_2 = Book.builder()
            .id(2L)
            .title("The Kreutzer Sonata")
            .publicationYear(1977)
            .author(EXPECTED_AUTHOR_2)
            .genre(EXPECTED_GENRE_2)
            .build();

    private static final Book EXPECTED_BOOK_3 = Book.builder()
            .id(3L)
            .title("The Ingenious Gentleman Don Quixote of La Mancha")
            .publicationYear(1605)
            .author(EXPECTED_AUTHOR_3)
            .genre(EXPECTED_GENRE_1)
            .build();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @BeforeEach
    public void setUp() {
        when(bookRepository.findById(EXPECTED_BOOK_1.getId())).thenReturn(Optional.of(EXPECTED_BOOK_1));
        when(bookRepository.findById(EXPECTED_BOOK_2.getId())).thenReturn(Optional.of(EXPECTED_BOOK_2));
        when(bookRepository.findById(EXPECTED_BOOK_3.getId())).thenReturn(Optional.of(EXPECTED_BOOK_3));
        when(bookRepository.findAll()).thenReturn(List.of(EXPECTED_BOOK_1, EXPECTED_BOOK_2, EXPECTED_BOOK_3));
        when(authorRepository.findAll()).thenReturn(List.of(EXPECTED_AUTHOR_1, EXPECTED_AUTHOR_2, EXPECTED_AUTHOR_3));
        when(genreRepository.findAll()).thenReturn(List.of(EXPECTED_GENRE_1, EXPECTED_GENRE_2, EXPECTED_GENRE_3));
    }

    @Test
    void shouldReturnExpectedBooks() throws Exception {

        List<Book> expected = List.of(EXPECTED_BOOK_1, EXPECTED_BOOK_2, EXPECTED_BOOK_3);

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

    static class BookListMatcher extends AssertionMatcher<List<Book>> {

        private final List<Book> expected;

        public BookListMatcher(List<Book> expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(List<Book> actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().build())
                    .containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    static class BookMatcher extends AssertionMatcher<Book> {

        private final Book expected;

        public BookMatcher(Book expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(Book actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    static class AuthorListMatcher extends AssertionMatcher<List<Author>> {

        private final List<Author> expected;

        public AuthorListMatcher(List<Author> expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(List<Author> actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().build())
                    .containsExactlyInAnyOrderElementsOf(expected);
        }
    }


    static class GenreListMatcher extends AssertionMatcher<List<Genre>> {

        private final List<Genre> expected;

        public GenreListMatcher(List<Genre> expected) {
            this.expected = expected;
        }

        @Override
        public void assertion(List<Genre> actual) throws AssertionError {
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().build())
                    .containsExactlyInAnyOrderElementsOf(expected);
        }
    }
}
