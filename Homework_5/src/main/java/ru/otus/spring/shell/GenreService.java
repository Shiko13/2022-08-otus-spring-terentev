package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.IOService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;
    private final IOService ioService;

    @ShellMethod(value = "Count of genres", key = {"genre_count", "g_count", "g_c"})
    private void getAmountOfGenres() {
        ioService.out(String.valueOf(genreDao.count()));
    }

    @ShellMethod(value = "Add new genre", key = {"genre_add", "g_add", "g_a"})
    private void addGenre(String name) {
        Genre genre = Genre.builder().name(name).build();
        ioService.out("id = " + genreDao.insert(genre));
    }

    @ShellMethod(value = "Get genre by id", key = {"genre_get_by_id", "g_get_by_id", "g_gbi"})
    private void getGenreById(long id) {
        Genre genre = genreDao.getById(id);
        ioService.out(genre.toString());
    }

    @ShellMethod(value = "Get all genres", key = {"genre_get_all", "g_get_all", "g_ga"})
    private void getAllGenres() {
        List<Genre> genres = genreDao.getAll();
        for (Genre genre : genres) {
            ioService.out(genre.toString());
        }
    }

    @ShellMethod(value = "Delete genre by id", key = {"genre_delete_by_id", "g_delete_by_id", "g_dbi"})
    private void deleteGenreById(long id) {
        genreDao.deleteById(id);
        ioService.out(String.format("Genre with id = %s deleted", id));
    }
}
