package ru.otus.spring.dto.formatter;

import org.springframework.format.Formatter;
import ru.otus.spring.dto.BookDtoOnlyId;

import java.util.Locale;

public class BookShortFormatter implements Formatter<BookDtoOnlyId> {

    @Override
    public BookDtoOnlyId parse(String id, Locale locale) {
        return new BookDtoOnlyId(Long.parseLong(id));
    }

    @Override
    public String print(BookDtoOnlyId book, Locale locale) {
        return (book.getId() == null) ? "" : book.getId().toString();
    }
}
