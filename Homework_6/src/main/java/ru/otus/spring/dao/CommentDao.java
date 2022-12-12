package ru.otus.spring.dao;

import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentDao {
    long count();
    Comment insert(Comment comment);
    Comment getById(long id);
    void deleteById(long id);
    List<Comment> getByBookId(long bookId);
}
