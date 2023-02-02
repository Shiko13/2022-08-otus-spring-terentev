package ru.otus.spring.service;

import ru.otus.spring.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    long count();
    AuthorDto insert(AuthorDto authorDto);
    Optional<AuthorDto> getById(long id);
    List<AuthorDto> getAll();
    void deleteById(long id);
}
