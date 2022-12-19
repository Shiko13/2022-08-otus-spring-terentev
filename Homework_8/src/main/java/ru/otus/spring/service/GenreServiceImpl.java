package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.domain.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public long count() {
        return genreRepository.count();
    }

    @Transactional
    @Override
    public Genre insert(Genre genre) {
        return genreRepository.save(genre);
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getById(String id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteByGenre_Id(id);
        genreRepository.deleteById(id);
    }
}