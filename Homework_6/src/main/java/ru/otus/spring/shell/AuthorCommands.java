package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorDao authorDao;

    @Transactional
    @ShellMethod(value = "Count of authors", key = {"author_count", "a_count", "a_c"})
    public long getAmountOfAuthors() {
        return authorDao.count();
    }

    @Transactional
    @ShellMethod(value = "Add new author", key = {"author_add", "a_add", "a_a"})
    public String addAuthor(String name, String surname) {
        Author author = Author.builder().name(name).surname(surname).build();
        return "id = " + authorDao.insert(author);
    }

    @Transactional
    @ShellMethod(value = "Get author by id", key = {"author_get_by_id", "a_get_by_id", "a_gbi"})
    public String getAuthorById(long id) {
        Author author = authorDao.getById(id);
        return author.toString();
    }

    @Transactional
    @ShellMethod(value = "Get all authors", key = {"author_get_all", "a_get_all", "a_ga"})
    public List<String> getAllAuthors() {
        List<Author> authors = authorDao.getAll();
        List<String> authorsToString = new ArrayList<>();
        for (Author author : authors) {
            authorsToString.add(author.toString());
        }
        return authorsToString;
    }

    @Transactional
    @ShellMethod(value = "Delete author by id", key = {"author_delete_by_id", "a_delete_by_id", "a_dbi"})
    public String deleteAuthorById(long id) {
        authorDao.deleteById(id);
        return String.format("Author with id = %s deleted", id);
    }
}