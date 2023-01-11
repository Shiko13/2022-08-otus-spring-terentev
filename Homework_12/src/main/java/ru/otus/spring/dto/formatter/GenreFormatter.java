package ru.otus.spring.dto.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import ru.otus.spring.dto.GenreDto;

import java.text.ParseException;
import java.util.Locale;

@Component
public class GenreFormatter implements Formatter<GenreDto> {
    @Override
    public GenreDto parse(String id, Locale locale) throws ParseException {
        return new GenreDto(Long.parseLong(id));
    }

    @Override
    public String print(GenreDto genre, Locale locale) {
        return (genre.getId() == null) ? "" : genre.getId().toString();
    }
}
