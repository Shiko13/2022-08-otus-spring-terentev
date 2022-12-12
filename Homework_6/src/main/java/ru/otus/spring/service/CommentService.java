package ru.otus.spring.service;

import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;

import java.util.List;

public interface CommentService {

    long count();
    Comment insert(Comment comment);
    Comment getById(long id);
    void deleteById(long id);
    List<CommentDto> getByBookId(long bookId);
}
