package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Long id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;

    public BookDto setId(Long id) {
        this.id = id;
        return this;
    }
}
