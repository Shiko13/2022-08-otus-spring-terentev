package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class CommentDto implements Serializable {
    private String id;
    private final String text;

    public CommentDto setId(String id) {
        this.id = id;
        return this;
    }
}