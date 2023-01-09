package ru.otus.spring.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;

@Component
public class CommentDtoConverter implements DtoConverter<Comment, CommentDto> {

    @Override
    public CommentDto toDto(Comment entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .text(entity.getText())
                .build();
    }

    @Override
    public Comment fromDto(CommentDto dto) {
        return Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .build();
    }
}