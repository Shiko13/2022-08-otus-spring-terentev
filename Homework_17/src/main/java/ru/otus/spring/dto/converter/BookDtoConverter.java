package ru.otus.spring.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;

@Component
public class BookDtoConverter implements DtoConverter<Book, BookDto> {

    private final DtoConverter<Author, AuthorDto> authorConverter;
    private final DtoConverter<Genre, GenreDto> genreConverter;

    public BookDtoConverter(DtoConverter<Author, AuthorDto> authorConverter,
                            DtoConverter<Genre, GenreDto> genreConverter) {
        this.authorConverter = authorConverter;
        this.genreConverter = genreConverter;
    }

    @Override
    public BookDto toDto(Book entity) {
        return new BookDto(entity.getId(), entity.getTitle(),
                authorConverter.toDto(entity.getAuthor()), genreConverter.toDto(entity.getGenre()));
    }

    @Override
    public Book fromDto(BookDto dto) {
        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor() == null ? null : authorConverter.fromDto(dto.getAuthor()))
                .genre(dto.getGenre() == null ? null : genreConverter.fromDto(dto.getGenre()))
                .build();
    }
}
