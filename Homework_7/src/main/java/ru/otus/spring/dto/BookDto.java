package ru.otus.spring.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BookDto implements Serializable {
    private final long id;
    private final String title;
    private final int publicationYear;
    private final AuthorDto author;
    private final GenreDto genre;
}