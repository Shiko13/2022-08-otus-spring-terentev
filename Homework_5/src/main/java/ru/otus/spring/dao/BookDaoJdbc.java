package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public long count() {
        Long count = jdbc.getJdbcOperations().
                queryForObject("select count(*) from books", Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public long insert(Book book) {
        var kh = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("publicationYear", book.getPublicationYear());
        params.addValue("authorId", book.getAuthor().getId());
        params.addValue("genreId", book.getGenre().getId());
        jdbc.update("insert into books (title, publication_year, author_id, genre_id) " +
                        "values (:title, :publicationYear, :authorId, :genreId)",
                params, kh, new String[]{"book_id"});
        return Objects.requireNonNull(kh.getKey()).longValue();
    }

    @Override
    public Book getById(long id) {
        return jdbc.queryForObject("""
                        select b.book_id, b.title, b.publication_year, a.author_id,\040
                        a.name, a.surname, g.genre_id, g.genre_name
                        from books as b
                        join authors as a on b.author_id = a.author_id
                        join genres as g on b.genre_id = g.genre_id
                        where b.book_id = :id""",
                Map.of("id", id), new BookMapper());
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("""
                        select b.book_id, b.title, b.publication_year, a.author_id,\040
                        a.name, a.surname, g.genre_id, g.genre_name
                        from books as b
                        join authors as a on b.author_id = a.author_id
                        join genres as g on b.genre_id = g.genre_id
                        """, new BookMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where book_id = :id", Map.of("id", id));
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return Book.builder()
                    .id(resultSet.getLong("book_id"))
                    .title(resultSet.getString("title"))
                    .publicationYear(resultSet.getInt("publication_year"))
                    .author(Author.builder()
                                .id(resultSet.getLong("author_id"))
                                .name(resultSet.getString("name"))
                                .surname(resultSet.getString("surname"))
                                .build())
                    .genre(Genre.builder()
                            .id(resultSet.getLong("genre_id"))
                            .name(resultSet.getString("genre_name"))
                            .build())
                    .build();
        }
    }
}
