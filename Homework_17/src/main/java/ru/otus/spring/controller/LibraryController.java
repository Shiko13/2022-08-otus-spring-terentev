package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LibraryController {

    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping(value = "/")
    public String mainPage(Model model) {
        List<AuthorDto> authors = authorService.getAll();
        List<GenreDto> genres = genreService.getAll();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "library";
    }
}