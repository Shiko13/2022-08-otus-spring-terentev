package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;

import java.util.List;

public interface GenreService {

    long count();
    Genre insert(Genre genre);
    Genre getById(String id);
    List<Genre> getAll();
    void deleteById(String id);
}
