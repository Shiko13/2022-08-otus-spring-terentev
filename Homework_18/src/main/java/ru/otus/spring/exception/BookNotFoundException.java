package ru.otus.spring.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String bookId) {
        super(String.format("Book %s not found", bookId));
    }
}
