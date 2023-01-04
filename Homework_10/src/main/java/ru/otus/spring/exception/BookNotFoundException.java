package ru.otus.spring.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(long bookId) {
        super(String.format("Book %s not found", bookId));
    }
}
