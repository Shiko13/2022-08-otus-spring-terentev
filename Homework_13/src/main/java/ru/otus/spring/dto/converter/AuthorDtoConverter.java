package ru.otus.spring.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;

@Component
public class AuthorDtoConverter implements DtoConverter<Author, AuthorDto> {

    @Override
    public AuthorDto toDto(Author entity) {
        return new AuthorDto(entity.getId(), entity.getName());
    }

    @Override
    public Author fromDto(AuthorDto dto) {
        return new Author(dto.getId(), dto.getName());
    }
}
