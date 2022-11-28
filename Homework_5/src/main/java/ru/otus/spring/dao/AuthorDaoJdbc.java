package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public long count() {
        Long count = jdbc.getJdbcOperations().
                queryForObject("select count(*) from authors", Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public long insert(Author author) {
        var kh = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource();
        params.addValue("name", author.getName());
        params.addValue("surname", author.getSurname());
        jdbc.update("insert into authors (name, surname) values (:name, :surname)",
                params, kh, new String[]{"author_id"});
        return Objects.requireNonNull(kh.getKey()).longValue();
    }

    @Override
    public Author getById(long id) {
        return jdbc.queryForObject("select author_id, name, surname from authors where author_id = :id",
        Map.of("id", id), new AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select author_id, name, surname from authors", new AuthorMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from authors where author_id = :id", Map.of("id", id));
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return Author.builder()
                                .id(resultSet.getLong("author_id"))
                                .name(resultSet.getString("name"))
                                .surname(resultSet.getString("surname"))
                                .build();
        }
    }
}
