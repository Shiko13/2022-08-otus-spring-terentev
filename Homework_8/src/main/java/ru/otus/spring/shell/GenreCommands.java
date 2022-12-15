package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.GenreService;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {
    private final GenreService genreService;

    @ShellMethod(value = "Count of genres", key = {"genre_count", "g_count", "g_c"})
    public long getAmountOfGenres() {
        return genreService.count();
    }

    @ShellMethod(value = "Add new genre", key = {"genre_add", "g_add", "g_a"})
    public String addGenre(String name) {
        Genre genre = new Genre(name);
        return "Genre = " + genreService.insert(genre);
    }

    @ShellMethod(value = "Get genre by id", key = {"genre_get_by_id", "g_get_by_id", "g_gbi"})
    public String getGenreById(String id) {
        Genre genre = genreService.getById(id);
        return genre.toString();
    }

    @ShellMethod(value = "Get all genres", key = {"genre_get_all", "g_get_all", "g_ga"})
    public List<String> getAllGenres() {
        List<Genre> genres = genreService.getAll();
        List<String> genresToString = new ArrayList<>();
        for (Genre genre : genres) {
            genresToString.add(genre.toString());
        }
        return genresToString;
    }

    @ShellMethod(value = "Delete genre by id", key = {"genre_delete_by_id", "g_delete_by_id", "g_dbi"})
    public String deleteGenreById(String id) {
        genreService.deleteById(id);
        return String.format("Genre with id = %s deleted", id);
    }
}