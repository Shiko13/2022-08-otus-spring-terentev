package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookDao {
    long count();
    void insert(Book book);
    Book getById(long id);
    List<Book> getAll();
    void deleteById(long id);
}
