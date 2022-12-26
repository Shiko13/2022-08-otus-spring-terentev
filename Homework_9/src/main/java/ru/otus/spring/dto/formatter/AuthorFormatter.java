package ru.otus.spring.dto.formatter;

import org.springframework.format.Formatter;
import ru.otus.spring.dto.AuthorDto;

import java.util.Locale;

public class AuthorFormatter implements Formatter<AuthorDto> {

    @Override
    public AuthorDto parse(String id, Locale locale) {
        return new AuthorDto(Long.parseLong(id));
    }

    @Override
    public String print(AuthorDto author, Locale locale) {
        return (author.getId() == null) ? "" : author.getId().toString();
    }
}
