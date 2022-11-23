package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.service.IOService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorDao authorDao;
    private final IOService ioService;

    @ShellMethod(value = "Count of authors", key = {"author_count", "a_count", "a_c"})
    private void getAmountOfAuthors() {
        ioService.out(String.valueOf(authorDao.count()));
    }

    @ShellMethod(value = "Add new author", key = {"author_add", "a_add", "a_a"})
    private void addAuthor(String name, String surname) {
        Author author = Author.builder().name(name).surname(surname).build();
        ioService.out("id = " + authorDao.insert(author));
    }

    @ShellMethod(value = "Get author by id", key = {"author_get_by_id", "a_get_by_id", "a_gbi"})
    private void getAuthorById(long id) {
        Author author = authorDao.getById(id);
        ioService.out(author.toString());
    }

    @ShellMethod(value = "Get all authors", key = {"author_get_all", "a_get_all", "a_ga"})
    private void getAllAuthors() {
        List<Author> authors = authorDao.getAll();
        for (Author author : authors) {
            ioService.out(author.toString());
        }
    }

    @ShellMethod(value = "Delete author by id", key = {"author_delete_by_id", "a_delete_by_id", "a_dbi"})
    private void deleteAuthorById(long id) {
        authorDao.deleteById(id);
        ioService.out(String.format("Author with id = %s deleted", id));
    }
}