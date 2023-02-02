package ru.otus.spring.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(long commentId) {
        super(String.format("Comment %s not found", commentId));
    }
}
