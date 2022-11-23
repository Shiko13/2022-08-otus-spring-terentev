package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTest {
    private static final int EXPECTED_BOOKS_COUNT = 3;
    private static final long EXISTING_BOOK_ID_1 = 1L;
    private static final String EXISTING_BOOK_TITLE_1 = "The Silmarillion";
    private static final int EXISTING_BOOK_PUBLICATION_YEAR_1 = 1977;
    private static final long EXISTING_BOOK_AUTHOR_ID_1 = 1L;
    private static final String EXISTING_AUTHOR_NAME_1 = "John";
    private static final String EXISTING_AUTHOR_SURNAME_1 = "Tolkien";
    private static final long EXISTING_BOOK_GENRE_ID_1 = 1L;
    private static final String EXISTING_GENRE_NAME_1 = "Novel";
    private static final long EXISTING_BOOK_ID_2 = 2L;
    private static final String EXISTING_BOOK_TITLE_2 = "The Kreutzer Sonata";
    private static final int EXISTING_BOOK_PUBLICATION_YEAR_2 = 1889;
    private static final long EXISTING_BOOK_AUTHOR_ID_2 = 2L;
    private static final String EXISTING_AUTHOR_NAME_2 = "Lev";
    private static final String EXISTING_AUTHOR_SURNAME_2 = "Tolstoy";
    private static final long EXISTING_BOOK_GENRE_ID_2 = 2L;
    private static final String EXISTING_GENRE_NAME_2 = "Novella";
    private static final long EXISTING_BOOK_ID_3 = 3L;
    private static final String EXISTING_BOOK_TITLE_3 = "The Ingenious Gentleman Don Quixote of La Mancha";
    private static final int EXISTING_BOOK_PUBLICATION_YEAR_3 = 1605;
    private static final long EXISTING_BOOK_AUTHOR_ID_3 = 3L;
    private static final String EXISTING_AUTHOR_NAME_3 = "Miguel";
    private static final String EXISTING_AUTHOR_SURNAME_3 = "Cervantes";
    private static final long EXISTING_BOOK_GENRE_ID_3 = 3L;
    private static final String EXISTING_GENRE_NAME_3 = "Fantasy";


    @Autowired
    private BookDaoJdbc bookDao;

    @Test
    void shouldReturnExpectedBookCount() {
        long actualBookCount = bookDao.count();
        assertThat(actualBookCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @Test
    void shouldInsertBook() {
        long expectedId = 4L;
        Book expectedBook = Book.builder()
                .id(expectedId)
                .title("The Lord of the Rings")
                .publicationYear(1954)
                .author(Author.builder()
                        .id(EXISTING_BOOK_AUTHOR_ID_1)
                        .name(EXISTING_AUTHOR_NAME_1)
                        .surname(EXISTING_AUTHOR_SURNAME_1)
                        .build()
                )
                .genre(Genre.builder()
                        .id(EXISTING_BOOK_GENRE_ID_3)
                        .name(EXISTING_GENRE_NAME_3)
                        .build())
                .build();
        bookDao.insert(expectedBook);
        Book actualBook = bookDao.getById(expectedId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldReturnExpectedBookById() {
        Book expectedBook = Book.builder()
                .id(EXISTING_BOOK_ID_1)
                .title(EXISTING_BOOK_TITLE_1)
                .publicationYear(EXISTING_BOOK_PUBLICATION_YEAR_1)
                .author(Author.builder()
                        .id(EXISTING_BOOK_AUTHOR_ID_1)
                        .name(EXISTING_AUTHOR_NAME_1)
                        .surname(EXISTING_AUTHOR_SURNAME_1)
                        .build())
                .genre(Genre.builder()
                        .id(EXISTING_BOOK_GENRE_ID_3)
                        .name(EXISTING_GENRE_NAME_3)
                        .build())
                .build();
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldCorrectDeleteAuthorById() {
        assertThatCode(() -> bookDao.getById(EXISTING_BOOK_ID_1))
                .doesNotThrowAnyException();

        bookDao.deleteById(EXISTING_BOOK_ID_1);

        assertThatThrownBy(() -> bookDao.getById(EXISTING_BOOK_ID_1))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldReturnExpectedAuthorList() {
        Book book1 = Book.builder()
                .id(EXISTING_BOOK_ID_1)
                .title(EXISTING_BOOK_TITLE_1)
                .publicationYear(EXISTING_BOOK_PUBLICATION_YEAR_1)
                .author(Author.builder()
                        .id(EXISTING_BOOK_AUTHOR_ID_1)
                        .name(EXISTING_AUTHOR_NAME_1)
                        .surname(EXISTING_AUTHOR_SURNAME_1)
                        .build())
                .genre(Genre.builder()
                        .id(EXISTING_BOOK_GENRE_ID_3)
                        .name(EXISTING_GENRE_NAME_3)
                        .build())
                .build();
        Book book2 = Book.builder()
                .id(EXISTING_BOOK_ID_2)
                .title(EXISTING_BOOK_TITLE_2)
                .publicationYear(EXISTING_BOOK_PUBLICATION_YEAR_2)
                .author(Author.builder()
                        .id(EXISTING_BOOK_AUTHOR_ID_2)
                        .name(EXISTING_AUTHOR_NAME_2)
                        .surname(EXISTING_AUTHOR_SURNAME_2)
                        .build())
                .genre(Genre.builder()
                        .id(EXISTING_BOOK_GENRE_ID_2)
                        .name(EXISTING_GENRE_NAME_2)
                        .build())
                .build();
        Book book3 = Book.builder()
                .id(EXISTING_BOOK_ID_3)
                .title(EXISTING_BOOK_TITLE_3)
                .publicationYear(EXISTING_BOOK_PUBLICATION_YEAR_3)
                .author(Author.builder()
                        .id(EXISTING_BOOK_AUTHOR_ID_3)
                        .name(EXISTING_AUTHOR_NAME_3)
                        .surname(EXISTING_AUTHOR_SURNAME_3)
                        .build())
                .genre(Genre.builder()
                        .id(EXISTING_BOOK_GENRE_ID_1)
                        .name(EXISTING_GENRE_NAME_1)
                        .build())
                .build();
        List<Book> actualBookList = bookDao.getAll();
        assertThat(actualBookList)
                .containsExactlyInAnyOrder(book1, book2, book3);
    }
}
