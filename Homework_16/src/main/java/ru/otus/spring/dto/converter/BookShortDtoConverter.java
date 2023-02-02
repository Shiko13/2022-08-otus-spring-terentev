package ru.otus.spring.dto.converter;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookShortDto;

@Component
@NoArgsConstructor
public class BookShortDtoConverter implements DtoConverter<Book, BookShortDto> {

    @Override
    public BookShortDto toDto(Book entity) {
        return new BookShortDto(entity.getId());
    }

    @Override
    public Book fromDto(BookShortDto dto) {
        return new Book(dto.id(), null, null, null, null);
    }
}
