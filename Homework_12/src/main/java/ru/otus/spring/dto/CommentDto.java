package ru.otus.spring.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CommentDto implements Serializable {
    private final long id;
    private final String text;
}