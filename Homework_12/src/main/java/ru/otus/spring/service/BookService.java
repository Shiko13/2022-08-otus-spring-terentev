package ru.otus.spring.service;

import ru.otus.spring.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    long count();
    BookDto insert(BookDto bookDto);
    Optional<BookDto> getById(long id);
    List<BookDto> getAll();
    void deleteById(long id);
    List<BookDto> getByAuthorId(long bookId);
}
