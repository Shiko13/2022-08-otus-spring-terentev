package ru.otus.spring.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.converter.AuthorDtoConverter;
import ru.otus.spring.repository.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorDtoConverter authorDtoConverter;


    @Override
    @Retry(name = "mongoDb")
    @TimeLimiter(name = "mongoDb")
    @CircuitBreaker(name = "mongoDb", fallbackMethod = "listAuthorsFallback")
    public Flux<AuthorDto> listAuthors() {
        return authorRepository.findAll()
                .map(authorDtoConverter::toDto);
    }

    private Flux<AuthorDto> listAuthorsFallback(Throwable t) {
        return Flux.just(new AuthorDto("00", "Without name"));
    }
}