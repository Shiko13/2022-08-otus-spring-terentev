package ru.otus.spring.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(long bookId) {
        super(String.format("Book with id = %s not found", bookId));
    }
}
