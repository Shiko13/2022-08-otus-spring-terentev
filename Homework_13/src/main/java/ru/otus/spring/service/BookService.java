package ru.otus.spring.service;

import ru.otus.spring.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookDto insert(BookDto book);

    BookDto update(BookDto book);

    void deleteById(long id);

    Optional<BookDto> getById(long id);

    List<BookDto> getAll();
}
