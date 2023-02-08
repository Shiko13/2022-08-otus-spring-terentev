package ru.otus.spring.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;

@Component
public class CommentDtoConverter implements DtoConverter<Comment, CommentDto> {

    @Override
    public CommentDto toDto(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getText()
        );
    }

    @Override
    public Comment fromDto(CommentDto dto) {
        return new Comment(
                dto.getId(),
                dto.getText()
        );
    }
}