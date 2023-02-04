package ru.otus.spring.service;

import reactor.core.publisher.Flux;
import ru.otus.spring.dto.GenreDto;

public interface GenreService {

    Flux<GenreDto> listGenres();
}
