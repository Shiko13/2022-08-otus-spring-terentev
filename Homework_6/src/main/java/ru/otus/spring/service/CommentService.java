package ru.otus.spring.service;

import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentService {

    long count();
    long insert(Comment comment);
    Comment getById(long id);
    List<Comment> getAll();
    void deleteById(long id);
}
