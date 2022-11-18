package ru.otus.spring.exceptions;

public class CsvRuntimeException extends RuntimeException {

    public CsvRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
