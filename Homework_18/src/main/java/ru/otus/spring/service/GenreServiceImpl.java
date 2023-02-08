package ru.otus.spring.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.converter.GenreDtoConverter;
import ru.otus.spring.repository.GenreRepository;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreDtoConverter genreDtoConverter;


    @Override
    @Retry(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb", fallbackMethod = "listGenresFallback")
    public Flux<GenreDto> listGenres() {
        return genreRepository.findAll()
                .map(genreDtoConverter::toDto);
    }

    private Flux<GenreDto> listGenresFallback(Throwable t) {
        return Flux.just(new GenreDto("000", "Without name"));
    }
}