package ru.otus.spring.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Data
@Builder
public class AuthorDto implements Serializable {
    private final String id;
    private final String name;
    private final String surname;
}