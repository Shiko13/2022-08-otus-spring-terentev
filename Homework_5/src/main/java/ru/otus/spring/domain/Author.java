package ru.otus.spring.domain;

import lombok.*;

@Data
@Builder
@ToString
public class Author {
    private final long id;
    private final String name;
    private final String surname;
}
