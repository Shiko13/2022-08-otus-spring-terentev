package ru.otus.spring.service;

import ru.otus.spring.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    long count();
    GenreDto insert(GenreDto genreDto);
    Optional<GenreDto> getById(long id);
    List<GenreDto> getAll();
    void deleteById(long id);
}
