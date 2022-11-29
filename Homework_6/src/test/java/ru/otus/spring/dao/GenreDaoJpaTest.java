package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Import(GenreDaoJpa.class)
public class GenreDaoJpaTest {

    private static final int EXPECTED_GENRES_COUNT = 3;
    private static final long EXISTING_GENRE_ID_1 = 1L;
    private static final String EXISTING_GENRE_NAME_1 = "Novel";
    private static final long EXISTING_GENRE_ID_2 = 2L;
    private static final String EXISTING_GENRE_NAME_2 = "Novella";
    private static final long EXISTING_GENRE_ID_3 = 3L;
    private static final String EXISTING_GENRE_NAME_3 = "Fantasy";

    @Autowired
    private GenreDaoJpa genreDao;

    @Test
    void shouldReturnExpectedGenreCount() {
        long actualGenreCount = genreDao.count();
        assertThat(actualGenreCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @Test
    void shouldInsertGenre() {
        long expectedId = 4L;
        Genre expectedGenre = Genre.builder()
                .id(expectedId)
                .name("Poem")
                .build();
        genreDao.insert(expectedGenre);
        Genre actualGenre = genreDao.getById(expectedId);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = Genre.builder()
                .id(EXISTING_GENRE_ID_1)
                .name(EXISTING_GENRE_NAME_1)
                .build();
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void shouldCorrectDeleteGenreById() {
        assertThatCode(() -> genreDao.getById(EXISTING_GENRE_ID_1))
                .doesNotThrowAnyException();

        genreDao.deleteById(EXISTING_GENRE_ID_1);
        assertThat(genreDao.getById(EXISTING_GENRE_ID_1)).isEqualTo(null);
    }

    @Test
    void shouldReturnExpectedGenreList() {
        Genre genre1 = Genre.builder()
                .id(EXISTING_GENRE_ID_1)
                .name(EXISTING_GENRE_NAME_1)
                .build();
        Genre genre2 = Genre.builder()
                .id(EXISTING_GENRE_ID_2)
                .name(EXISTING_GENRE_NAME_2)
                .build();
        Genre genre3 = Genre.builder()
                .id(EXISTING_GENRE_ID_3)
                .name(EXISTING_GENRE_NAME_3)
                .build();
        List<Genre> actualGenreList = genreDao.getAll();
        assertThat(actualGenreList)
                .containsExactlyInAnyOrder(genre1, genre2, genre3);
    }
}
