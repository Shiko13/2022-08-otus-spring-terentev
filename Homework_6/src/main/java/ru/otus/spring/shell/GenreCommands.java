package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {
    private final GenreDao genreDao;

    @Transactional
    @ShellMethod(value = "Count of genres", key = {"genre_count", "g_count", "g_c"})
    public long getAmountOfGenres() {
        return genreDao.count();
    }

    @Transactional
    @ShellMethod(value = "Add new genre", key = {"genre_add", "g_add", "g_a"})
    public String addGenre(String name) {
        Genre genre = Genre.builder().name(name).build();
        return "id = " + genreDao.insert(genre);
    }

    @Transactional
    @ShellMethod(value = "Get genre by id", key = {"genre_get_by_id", "g_get_by_id", "g_gbi"})
    public String getGenreById(long id) {
        Genre genre = genreDao.getById(id);
        return genre.toString();
    }

    @Transactional
    @ShellMethod(value = "Get all genres", key = {"genre_get_all", "g_get_all", "g_ga"})
    public List<String> getAllGenres() {
        List<Genre> genres = genreDao.getAll();
        List<String> genresToString = new ArrayList<>();
        for (Genre genre : genres) {
            genresToString.add(genre.toString());
        }
        return genresToString;
    }

    @Transactional
    @ShellMethod(value = "Delete genre by id", key = {"genre_delete_by_id", "g_delete_by_id", "g_dbi"})
    public String deleteGenreById(long id) {
        genreDao.deleteById(id);
        return String.format("Genre with id = %s deleted", id);
    }
}