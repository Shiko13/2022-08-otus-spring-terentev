package ru.otus.spring.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.BookShortDto;
import ru.otus.spring.dto.CommentDto;

@Component
public class CommentDtoConverter implements DtoConverter<Comment, CommentDto> {

    private final DtoConverter<Book, BookShortDto> bookConverter;

    public CommentDtoConverter(DtoConverter<Book, BookShortDto> bookConverter) {
        this.bookConverter = bookConverter;
    }

    @Override
    public CommentDto toDto(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getText(),
                bookConverter.toDto(entity.getBook())
        );
    }

    @Override
    public Comment fromDto(CommentDto dto) {
        return new Comment(
                dto.getId(),
                dto.getText(),
                bookConverter.fromDto(dto.getBook())
        );
    }
}