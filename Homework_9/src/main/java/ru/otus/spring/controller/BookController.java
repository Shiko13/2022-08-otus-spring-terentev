package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.error.NotFoundException;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.Optional;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @GetMapping
    public String booksPage(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "book/list";
    }

    @GetMapping("/add")
    public String savePage(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "book/add";
    }

    @PostMapping("/add")
    public String save(Optional<Book> bookOptional,
                       @RequestParam(value = "authorId") long authorId,
                       @RequestParam(value = "genreId") long genreId) {
        Book book = bookOptional.orElseThrow(NotFoundException::new);
        Author author = authorRepository.findById(authorId).orElseThrow(NotFoundException::new);
        Genre genre = genreRepository.findById(genreId).orElseThrow(NotFoundException::new);
        book.setAuthor(author);
        book.setGenre(genre);
        bookRepository.save(book);
        return "redirect:/book";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(value = "id") long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "book/edit";
    }

    @PostMapping("/edit")
    public String edit(Book book,
                       @RequestParam(value = "authorId") long authorId,
                       @RequestParam(value = "genreId") long genreId) {
        Book editeBook = bookRepository.findById(book.getId()).orElseThrow(NotFoundException::new);
        Author author = authorRepository.findById(authorId).orElseThrow(NotFoundException::new);
        Genre genre = genreRepository.findById(genreId).orElseThrow(NotFoundException::new);
        editeBook.setTitle(book.getTitle());
        editeBook.setPublicationYear(book.getPublicationYear());
        editeBook.setAuthor(author);
        editeBook.setGenre(genre);
        bookRepository.save(editeBook);
        return "redirect:/book";
    }

    @PostMapping("/remove")
    public String delete(@RequestParam(value = "id") long id) {
        Book book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        bookRepository.delete(book);
        return "redirect:/book";
    }
}