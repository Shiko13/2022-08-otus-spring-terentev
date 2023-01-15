package ru.otus.spring.service;

import ru.otus.spring.dto.CommentDto;

public interface AuthenticationService {
    CommentDto setPermission(CommentDto commentDto);
}
