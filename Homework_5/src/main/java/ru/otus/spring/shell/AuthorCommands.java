package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorDao authorDao;

    @ShellMethod(value = "Count of authors", key = {"author_count", "a_count", "a_c"})
    private long getAmountOfAuthors() {
        return authorDao.count();
    }

    @ShellMethod(value = "Add new author", key = {"author_add", "a_add", "a_a"})
    private String addAuthor(String name, String surname) {
        Author author = Author.builder().name(name).surname(surname).build();
        return "id = " + authorDao.insert(author);
    }

    @ShellMethod(value = "Get author by id", key = {"author_get_by_id", "a_get_by_id", "a_gbi"})
    private String getAuthorById(long id) {
        Author author = authorDao.getById(id);
        return author.toString();
    }

    @ShellMethod(value = "Get all authors", key = {"author_get_all", "a_get_all", "a_ga"})
    private List<String> getAllAuthors() {
        List<Author> authors = authorDao.getAll();
        List<String> authorsToString = new ArrayList<>();
        for (Author author : authors) {
            authorsToString.add(author.toString());
        }
        return authorsToString;
    }

    @ShellMethod(value = "Delete author by id", key = {"author_delete_by_id", "a_delete_by_id", "a_dbi"})
    private String deleteAuthorById(long id) {
        authorDao.deleteById(id);
        return String.format("Author with id = %s deleted", id);
    }
}