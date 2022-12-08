package ru.otus.spring.service;

import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentService {

    long count();
    long insert(Comment comment);
    Comment getById(long id);
    void deleteById(long id);
    List<Comment> getByBookId(long bookId);
}
