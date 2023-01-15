package ru.otus.spring.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;

@Component
@RequiredArgsConstructor
public class BookDtoConverter implements DtoConverter<Book, BookDto> {

    private final DtoConverter<Author, AuthorDto> authorConverter;
    private final DtoConverter<Genre, GenreDto> genreConverter;

    @Override
    public BookDto toDto(Book entity) {
        return new BookDto(
            entity.getId(),
            entity.getTitle(),
            authorConverter.toDto(entity.getAuthor()),
            genreConverter.toDto(entity.getGenre())
        );
    }

    @Override
    public Book fromDto(BookDto dto) {
        return new Book(
            dto.getId(),
            dto.getTitle(),
            (dto.getAuthor() == null) ? null : authorConverter.fromDto(dto.getAuthor()),
            (dto.getGenre() == null) ? null : genreConverter.fromDto(dto.getGenre()),
            null
        );
    }
}