package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.Author;
import ru.otus.spring.service.AuthorService;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;

    @ShellMethod(value = "Count of authors", key = {"author_count", "a_count", "a_c"})
    public long getAmountOfAuthors() {
        return authorService.count();
    }

    @ShellMethod(value = "Add new author", key = {"author_add", "a_add", "a_a"})
    public String addAuthor(String name, String surname) {
        Author author = new Author(name, surname);
        return "Author = " + authorService.insert(author);
    }

    @ShellMethod(value = "Get author by id", key = {"author_get_by_id", "a_get_by_id", "a_gbi"})
    public String getAuthorById(String id) {
        Author author = authorService.getById(id);
        return author.toString();
    }

    @ShellMethod(value = "Get all authors", key = {"author_get_all", "a_get_all", "a_ga"})
    public List<String> getAllAuthors() {
        List<Author> authors = authorService.getAll();
        List<String> authorsToString = new ArrayList<>();
        for (Author author : authors) {
            authorsToString.add(author.toString());
        }
        return authorsToString;
    }

    @ShellMethod(value = "Delete author by id", key = {"author_delete_by_id", "a_delete_by_id", "a_dbi"})
    public String deleteAuthorById(String id) {
        authorService.deleteById(id);
        return String.format("Author with id = %s deleted", id);
    }
}