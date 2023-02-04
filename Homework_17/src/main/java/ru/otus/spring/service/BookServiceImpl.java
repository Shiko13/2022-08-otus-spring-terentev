package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.exception.BookNotFoundException;
import ru.otus.spring.repository.BookRepository;
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

    @Transactional
    @Override
    public long count() {
        return bookRepository.count();
    }

    @Transactional
    @Override
    public BookDto insert(BookDto bookDto) {
        Book book = bookConverter.fromDto(bookDto);
        return bookConverter.toDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> getById(long id) {
        return bookRepository.findById(id).map(bookConverter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(bookConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteByBook_Id(id);
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> getByAuthorId(long authorId) {
        return bookRepository.findByAuthor_Id(authorId)
                .stream()
                .map(bookConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto update(BookDto bookDto) {
        Book book = bookConverter.fromDto(bookDto);
        if (book.getId() == null || bookRepository.findById(bookDto.getId()).isEmpty()) {
            throw new BookNotFoundException(book.getId());
        }
        return bookConverter.toDto(bookRepository.save(book));
    }
}