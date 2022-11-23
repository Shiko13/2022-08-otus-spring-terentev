package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public long count() {
        Long count = jdbc.getJdbcOperations().
                queryForObject("select count(*) from genres", Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public long insert(Genre genre) {
        var kh = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());
        jdbc.update("insert into genres (genre_name) values (:name)",
                params, kh, new String[]{"genre_id"});
        return Objects.requireNonNull(kh.getKey()).longValue();
    }

    @Override
    public Genre getById(long id) {
        return jdbc.queryForObject("select genre_id, genre_name from genres where genre_id = :id",
                Map.of("id", id), new GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select genre_id, genre_name from genres", new GenreMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from genres where genre_id = :id", Map.of("id", id));
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return Genre.builder()
                    .id(resultSet.getLong("genre_id"))
                    .name(resultSet.getString("genre_name"))
                    .build();
        }
    }
}
