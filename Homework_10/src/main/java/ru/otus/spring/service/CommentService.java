package ru.otus.spring.service;

import ru.otus.spring.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    long count();
    CommentDto insert(CommentDto commentDto);
    Optional<CommentDto> getById(long id);
    void deleteById(long id);
    List<CommentDto> getByBookId(long bookId);
    CommentDto update(CommentDto commentDto);
}
