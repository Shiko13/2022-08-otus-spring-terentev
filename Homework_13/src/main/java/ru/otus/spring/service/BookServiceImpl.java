package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;
    private final DtoConverter<Book, BookDto> bookConverter;


    @Override
    @Transactional
    public BookDto insert(BookDto bookDto) {
        Book book = bookConverter.fromDto(bookDto);
        return bookConverter.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookDto bookDto) {
        Book book = bookConverter.fromDto(bookDto);
        return bookConverter.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteByBook_Id(id);
        bookRepository.deleteById(id);
    }

    @Override
    public Optional<BookDto> getById(long id) {
        return bookRepository.findById(id).map(bookConverter::toDto);
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(bookConverter::toDto)
                .collect(Collectors.toList());
    }
}
