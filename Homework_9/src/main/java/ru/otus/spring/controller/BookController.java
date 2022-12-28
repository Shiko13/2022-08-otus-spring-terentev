package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.error.NotFoundException;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.List;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final AuthorService authorService;
    private final BookService bookService;
    private final GenreService genreService;

    @GetMapping
    public String booksPage(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "book/list";
    }

    @GetMapping("/add")
    public String savePage(Model model) {
        List<AuthorDto> authors = authorService.getAll();
        List<GenreDto> genres = genreService.getAll();
        model.addAttribute("book", new BookDto(null, "", 2022,
                new AuthorDto(null, null, null), new GenreDto(null, null)));
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/add";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("book") BookDto bookDto) {
        bookService.insert(bookDto);
        return "redirect:/book";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(value = "id") long id, Model model) {
        BookDto book = bookService.getById(id).orElseThrow(NotFoundException::new);
        List<AuthorDto> authors = authorService.getAll();
        List<GenreDto> genres = genreService.getAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") BookDto bookDto) {
        bookService.insert(bookDto);
        return "redirect:/book";
    }

    @PostMapping("/remove")
    public String delete(@RequestParam(value = "id") long id) {
        bookService.deleteById(id);
        return "redirect:/book";
    }
}