package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    long count();
    Book insert(Book book);
    Optional<Book> getById(String id);
    List<Book> getAll();
    void deleteById(String id);
    List<BookDto> getByAuthorId(String bookId);
}
