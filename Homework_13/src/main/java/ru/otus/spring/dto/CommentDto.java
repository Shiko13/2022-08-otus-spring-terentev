package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private final String text;
    private final BookShortDto book;

    public CommentDto setId(Long id) {
        this.id = id;
        return this;
    }
}
