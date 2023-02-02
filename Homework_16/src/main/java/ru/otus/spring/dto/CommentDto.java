package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class CommentDto implements Serializable {
    private Long id;
    private final String text;
    private final BookShortDto book;
}