package ru.otus.spring.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.otus.spring.domain.Genre} entity
 */
@Data
@Builder
public class GenreDto implements Serializable {
    private final String id;
    private final String name;
}