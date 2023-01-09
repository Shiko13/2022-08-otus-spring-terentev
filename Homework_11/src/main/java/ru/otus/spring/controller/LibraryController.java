package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.dto.converter.AuthorDtoConverter;
import ru.otus.spring.dto.converter.GenreDtoConverter;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.GenreRepository;

@Controller
@RequiredArgsConstructor
public class LibraryController {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final AuthorDtoConverter authorDtoConverter;
    private final GenreDtoConverter genreDtoConverter;

    @GetMapping(value = "/")
    public String mainPage(Model model) {
        model.addAttribute("authors", authorRepository.findAll()
                .map(authorDtoConverter::toDto)
                .collectList());
        model.addAttribute("genres", genreRepository.findAll()
                .map(genreDtoConverter::toDto)
                .collectList());
        return "library";
    }
}