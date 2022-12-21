package ru.otus.spring.service;

import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;

import java.util.List;

public interface CommentService {

    long count();
    Comment insert(Comment comment);
    Comment getById(String id);
    void deleteById(String id);
    List<CommentDto> getByBookId(String bookId);
}
