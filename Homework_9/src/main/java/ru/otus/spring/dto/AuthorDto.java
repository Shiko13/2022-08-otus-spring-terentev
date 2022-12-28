package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class AuthorDto implements Serializable {
    private Long id;
    private String name;
    private String surname;

    public AuthorDto(long id) {
        this(id, null, null);
    }
}