package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.exception.BookNotFoundException;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;


    @GetMapping("/book")
    public String listBooks(Model model) {
        List<BookDto> books = bookService.getAll();
        model.addAttribute("books", books);
        return "list_books";
    }

    @PostMapping("/book/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/book";
    }

    @GetMapping("/book/edit")
    public String editBook(@RequestParam("id") long id, Model model) {
        BookDto book = bookService.getById(id).orElseThrow(() -> new BookNotFoundException(id));
        List<AuthorDto> authors = authorService.getAll();
        List<GenreDto> genres = genreService.getAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "edit_book";
    }

    @PostMapping("/book/edit")
    public String editBook(@ModelAttribute("book") BookDto book) {
        bookService.update(book);
        return "redirect:/book";
    }

    @GetMapping("/book/create")
    public String createBook(Model model) {
        List<AuthorDto> authors = authorService.getAll();
        List<GenreDto> genres = genreService.getAll();
        model.addAttribute("book", new BookDto(null, "",
                new AuthorDto(null), new GenreDto(null)));
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "edit_book";
    }

    @PostMapping("/book/create")
    public String createBook(@ModelAttribute("book") BookDto book) {
        bookService.insert(book);
        return "redirect:/book";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}
