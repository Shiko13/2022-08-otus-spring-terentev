package ru.otus.spring.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Data
@Builder
public class CommentDto implements Serializable {
    private final String id;
    private final String text;
}