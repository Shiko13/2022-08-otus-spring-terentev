package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto implements Serializable {

    private String id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;

    public BookDto setId(String id) {
        this.id = id;
        return this;
    }
}