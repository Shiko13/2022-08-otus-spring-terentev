package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Import(AuthorDaoJpa.class)
public class AuthorDaoJpaTest {

    private static final int EXPECTED_AUTHORS_COUNT = 3;
    private static final long EXISTING_AUTHOR_ID_1 = 1L;
    private static final String EXISTING_AUTHOR_NAME_1 = "John";
    private static final String EXISTING_AUTHOR_SURNAME_1 = "Tolkien";
    private static final long EXISTING_AUTHOR_ID_2 = 2L;
    private static final String EXISTING_AUTHOR_NAME_2 = "Lev";
    private static final String EXISTING_AUTHOR_SURNAME_2 = "Tolstoy";
    private static final long EXISTING_AUTHOR_ID_3 = 3L;
    private static final String EXISTING_AUTHOR_NAME_3 = "Miguel";
    private static final String EXISTING_AUTHOR_SURNAME_3 = "Cervantes";

    @Autowired
    private AuthorDaoJpa authorDao;

    @Autowired
    private TestEntityManager tem;

    @Test
    void shouldReturnExpectedAuthorCount() {
        long actualAuthorCount = authorDao.count();
        assertThat(actualAuthorCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @Test
    void shouldInsertAuthor() {
        Author expectedAuthor = tem.persistAndFlush(
                Author.builder()
                        .name("Jules")
                        .surname("Cortazar")
                        .build());
        Author actualAuthor = tem.find(Author.class, expectedAuthor.getId());

        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = Author.builder()
                .id(EXISTING_AUTHOR_ID_1)
                .name(EXISTING_AUTHOR_NAME_1)
                .surname(EXISTING_AUTHOR_SURNAME_1)
                .build();
        Author actualAuthor = tem.find(Author.class, expectedAuthor.getId());

        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void shouldCorrectDeleteAuthorById() {
        assertThatCode(() -> authorDao.getById(EXISTING_AUTHOR_ID_1))
                .doesNotThrowAnyException();

        authorDao.deleteById(EXISTING_AUTHOR_ID_1);
        assertThat(authorDao.getById(EXISTING_AUTHOR_ID_1)).isEqualTo(null);
    }

    @Test
    void shouldReturnExpectedAuthorList() {
        Author author1 = Author.builder()
                .id(EXISTING_AUTHOR_ID_1)
                .name(EXISTING_AUTHOR_NAME_1)
                .surname(EXISTING_AUTHOR_SURNAME_1)
                .build();
        Author author2 = Author.builder()
                .id(EXISTING_AUTHOR_ID_2)
                .name(EXISTING_AUTHOR_NAME_2)
                .surname(EXISTING_AUTHOR_SURNAME_2)
                .build();
        Author author3 = Author.builder()
                .id(EXISTING_AUTHOR_ID_3)
                .name(EXISTING_AUTHOR_NAME_3)
                .surname(EXISTING_AUTHOR_SURNAME_3)
                .build();
        List<Author> actualAuthorList = authorDao.getAll();

        assertThat(actualAuthorList)
                .containsExactlyInAnyOrder(author1, author2, author3);
    }
}