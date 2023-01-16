package ru.otus.spring.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.spring.domain.mongo.Author;
import ru.otus.spring.domain.mongo.Book;
import ru.otus.spring.domain.mongo.Comment;
import ru.otus.spring.domain.mongo.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.config.JobConfig.*;
import static ru.otus.spring.mongock.changelog.MongoDatabaseChangelog.*;

@SpringBootTest
@SpringBatchTest
public class CopyLibraryJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void fromMongoToH2JobExitStatus() throws Exception {
        Job job = jobLauncherTestUtils.getJob();

        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(COPY_LIBRARY_JOB_NAME);

        JobParameters parameters = new JobParametersBuilder()
                .addString(INIT_SCHEMA_FILE_NAME, appConfig.getInitSchemaFileName())
                .addString(UPDATE_SCHEMA_FILE_NAME, appConfig.getUpdateSchemaFileName())
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    void fromMongoToH2JobResults() throws Exception {
        List<Author> expectedAuthors = Stream.of(AUTHOR_1, AUTHOR_2, AUTHOR_3)
                .sorted(Comparator.comparing(Author::getName))
                .collect(Collectors.toList());
        List<Genre> expectedGenres = Stream.of(GENRE_1, GENRE_2, GENRE_3)
                .sorted(Comparator.comparing(Genre::getTitle))
                .collect(Collectors.toList());
        List<Book> expectedBooks = Stream.of(BOOK_1, BOOK_2, BOOK_3)
                .sorted(Comparator.comparing(Book::getTitle))
                .map(book -> new Book(book.getId(), book.getTitle(), book.getAuthor(),
                        book.getGenres().stream()
                                .sorted(Comparator.comparing(Genre::getTitle))
                                .collect(Collectors.toList()),
                        book.getComments().stream()
                                .sorted(Comparator.comparing(Comment::getText))
                                .collect(Collectors.toList()))
                )
                .collect(Collectors.toList());

        JobParameters parameters = new JobParametersBuilder()
                .addString(INIT_SCHEMA_FILE_NAME, appConfig.getInitSchemaFileName())
                .addString(UPDATE_SCHEMA_FILE_NAME, appConfig.getUpdateSchemaFileName())
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        List<Author> authors = jdbc.query("select id, name from author order by name",
                new AuthorMapper());

        assertThat(authors)
                .usingRecursiveComparison()
                .comparingOnlyFields("name")
                .isEqualTo(expectedAuthors);

        List<Genre> genres = jdbc.query("select id, title from genre order by title",
                new GenreMapper());

        assertThat(genres)
                .usingRecursiveComparison()
                .comparingOnlyFields("title")
                .isEqualTo(expectedGenres);

        List<Book> books = jdbc.query("" +
                "select b.id, b.title, b.author_id " +
                "from book b " +
                "order by title", new BookMapper(authors));

        Map<String, Book> booksMap = books.stream().collect(Collectors.toMap(Book::getId, b -> b));

        jdbc.query("" +
                "select book_id, id, comment_text " +
                "from book_comment b " +
                "order by book_id, comment_text", new BookCommentMapper(booksMap));

        jdbc.query("" +
                "select b2g.book_id, b2g.genre_id " +
                "from book_genre b2g join genre g on b2g.genre_id = g.id " +
                "order by b2g.book_id, g.title", new BookGenreMapper(booksMap, genres));

        assertThat(books)
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing(Book::getTitle)
                                .thenComparing(o -> o.getAuthor().getName())
                                .thenComparing((o1, o2) -> {
                                    int result = Integer.compare(o1.getGenres().size(), o2.getGenres().size());
                                    if (result == 0) {
                                        for (int i = 0; i < o1.getGenres().size(); i++) {
                                            result = o1.getGenres().get(i).getTitle().compareTo(o2.getGenres().get(i).getTitle());
                                            if (result != 0) {
                                                break;
                                            }
                                        }
                                    }
                                    return result;
                                })
                                .thenComparing((o1, o2) -> {
                                    int result = Integer.compare(o1.getComments().size(), o2.getComments().size());
                                    if (result == 0) {
                                        for (int i = 0; i < o1.getComments().size(); i++) {
                                            result = o1.getComments().get(i).getText()
                                                    .compareTo(o2.getComments().get(i).getText());
                                            if (result != 0) {
                                                break;
                                            }
                                        }
                                    }
                                    return result;
                                })
                        , Book.class)
                .isEqualTo(expectedBooks);
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Author(Long.toString(id), name);
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            return new Genre(Long.toString(id), title);
        }
    }

    private static class BookMapper implements RowMapper<Book> {

        private final Map<String, Author> authorsMap;

        public BookMapper(List<Author> authors) {
            this.authorsMap = authors.stream().collect(Collectors.toMap(Author::getId, a -> a));
        }

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            long authorId = resultSet.getLong("author_id");
            return new Book(Long.toString(id), title, authorsMap.get(Long.toString(authorId)), new ArrayList<>(), new ArrayList<>());
        }
    }

    private record BookCommentMapper(Map<String, Book> booksMap) implements RowMapper<Book> {

        @Override
            public Book mapRow(ResultSet resultSet, int i) throws SQLException {
                long id = resultSet.getLong("id");
                String commentText = resultSet.getString("comment_text");
                long bookId = resultSet.getLong("book_id");
                Book book = booksMap.get(Long.toString(bookId));
                book.getComments().add(new Comment(Long.toString(id), commentText));
                return book;
            }
        }

    private static class BookGenreMapper implements RowMapper<Book> {

        private final Map<String, Book> booksMap;
        private final Map<String, Genre> genresMap;

        public BookGenreMapper(Map<String, Book> booksMap, List<Genre> genres) {
            this.booksMap = booksMap;
            this.genresMap = genres.stream().collect(Collectors.toMap(Genre::getId, a -> a));
        }

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long bookId = resultSet.getLong("book_id");
            long genreId = resultSet.getLong("genre_id");
            Book book = booksMap.get(Long.toString(bookId));
            Genre genre = genresMap.get(Long.toString(genreId));
            book.getGenres().add(genre);
            return book;
        }
    }
}