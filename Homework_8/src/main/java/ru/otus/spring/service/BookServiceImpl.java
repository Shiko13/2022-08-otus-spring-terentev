package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CommentService commentService;
    private final DtoConverter<Book, BookDto> bookConverter;

    @Transactional
    @Override
    public long count() {
        return bookRepository.count();
    }

    @Transactional
    @Override
    public Book insert(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> getById(String id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentService.getByBookId(id).forEach(c -> commentService.deleteById(c.getId()));
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> getByAuthorId(String authorId) {
        return bookRepository.findByAuthor_Id(authorId)
                .stream()
                .map(bookConverter::toDto)
                .collect(Collectors.toList());
    }
}