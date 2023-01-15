package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@Builder
@ToString
@AllArgsConstructor
public class GenreDto {
    private Long id;
    private String name;

    public GenreDto(Long id) {
        this(id, null);
    }

    public GenreDto setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreDto genreDto = (GenreDto) o;
        return Objects.equals(id, genreDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
