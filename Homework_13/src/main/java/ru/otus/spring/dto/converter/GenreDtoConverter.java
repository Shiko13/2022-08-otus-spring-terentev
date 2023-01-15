package ru.otus.spring.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.GenreDto;

@Component
public class GenreDtoConverter implements DtoConverter<Genre, GenreDto> {

    @Override
    public GenreDto toDto(Genre entity) {
        return new GenreDto(entity.getId(), entity.getName());
    }

    @Override
    public Genre fromDto(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName());
    }
}
