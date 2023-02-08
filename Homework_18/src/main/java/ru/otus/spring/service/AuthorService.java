package ru.otus.spring.service;

import reactor.core.publisher.Flux;
import ru.otus.spring.dto.AuthorDto;

public interface AuthorService {

    Flux<AuthorDto> listAuthors();
}
