package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.converter.AuthorDtoConverter;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorDtoConverter authorDtoConverter;

    @Transactional
    @Override
    public long count() {
        return authorRepository.count();
    }

    @Transactional
    @Override
    public AuthorDto insert(AuthorDto authorDto) {
        Author author = authorDtoConverter.fromDto(authorDto);
        return authorDtoConverter.toDto(authorRepository.save(author));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AuthorDto> getById(long id) {
        return authorRepository.findById(id).map(authorDtoConverter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> getAll() {
        return authorRepository.findAll().stream()
                .map(authorDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteByAuthor_Id(id);
        authorRepository.deleteById(id);
    }
}
