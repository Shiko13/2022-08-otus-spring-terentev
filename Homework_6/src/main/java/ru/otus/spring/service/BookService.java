package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookService {

    long count();
    long insert(Book book);
    Book getById(long id);
    List<Book> getAll();
    void deleteById(long id);
}
