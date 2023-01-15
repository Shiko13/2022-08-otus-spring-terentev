package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.converter.DtoConverter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final DtoConverter<Genre, GenreDto> genreDtoConverter;


    @Transactional
    @Override
    public long count() {
        return genreRepository.count();
    }

    @Transactional
    @Override
    public GenreDto insert(GenreDto genreDto) {
        Genre genre = genreDtoConverter.fromDto(genreDto);
        return genreDtoConverter.toDto(genreRepository.save(genre));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreDto> getById(long id) {
        return genreRepository.findById(id).map(genreDtoConverter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> getAll() {
        return genreRepository.findAll().stream()
                .map(genreDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteByGenre_Id(id);
        genreRepository.deleteById(id);
    }
}
