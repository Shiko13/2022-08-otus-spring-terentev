package ru.otus.spring.service;

import ru.otus.spring.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto insert(CommentDto comment);

    CommentDto update(CommentDto comment);

    void deleteById(long id);

    Optional<CommentDto> getById(long id);

    List<CommentDto> getByBookId(long bookId);

}
