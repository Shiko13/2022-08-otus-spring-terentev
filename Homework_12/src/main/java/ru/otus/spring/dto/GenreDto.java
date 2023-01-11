package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class GenreDto implements Serializable {
    private Long id;
    private String name;

    public GenreDto(long id) {
        this(id, null);
    }
}