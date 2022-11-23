package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Book {
    private final long id;
    private final String title;
    private final int publicationYear;
    private final Author author;
    private final Genre genre;
}
