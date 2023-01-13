package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class AuthorDto {
    private Long id;
    private final String name;

    public AuthorDto(Long id) {
        this(id, null);
    }

    public AuthorDto setId(Long id) {
        this.id = id;
        return this;
    }
}
