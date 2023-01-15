package ru.otus.spring.dto.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import ru.otus.spring.dto.BookShortDto;

import java.util.Locale;

@Component
public class BookShortFormatter implements Formatter<BookShortDto> {

    @Override
    public BookShortDto parse(String id, Locale locale) {
        return new BookShortDto(Long.parseLong(id));
    }

    @Override
    public String print(BookShortDto book, Locale locale) {
        return (book.id() == null) ? "" : book.id().toString();
    }
}
