package ru.otus.spring.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(long commentId) {
        super(String.format("Comment with id = %s not found", commentId));
    }
}
