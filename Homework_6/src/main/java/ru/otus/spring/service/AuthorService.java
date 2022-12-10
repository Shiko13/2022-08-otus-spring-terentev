package ru.otus.spring.service;

import ru.otus.spring.domain.Author;

import java.util.List;

public interface AuthorService {

    long count();
    Author insert(Author author);
    Author getById(long id);
    List<Author> getAll();
    void deleteById(long id);
}
