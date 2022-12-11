package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    long count();
    Book insert(Book book);
    Optional<Book> getById(long id);
    List<Book> getAll();
    void deleteById(long id);
}
