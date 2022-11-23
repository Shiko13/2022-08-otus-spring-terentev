package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.IOService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BookService {

    private final BookDao bookDao;
    private final IOService ioService;

    @ShellMethod(value = "Count of books", key = {"book_count", "b_count", "b_c"})
    private void getAmountOfBooks() {
        ioService.out(String.valueOf(bookDao.count()));
    }

    @ShellMethod(value = "Add new book", key = {"book_add", "b_add", "b_a"})
    private void addBook(String title, int publicationYear, long authorId, long genreId) {
        Author author = Author.builder().id(authorId).build();
        Genre genre = Genre.builder().id(genreId).build();
        Book book = Book.builder()
                        .title(title)
                        .publicationYear(publicationYear)
                        .author(author)
                        .genre(genre)
                        .build();
        ioService.out("id = " + bookDao.insert(book));
        ioService.out(book.toString());
    }

    @ShellMethod(value = "Get book by id", key = {"book_get_by_id", "b_get_by_id", "b_gbi"})
    private void getBookById(long id) {
        Book book = bookDao.getById(id);
        ioService.out(book.toString());
    }

    @ShellMethod(value = "Get all books", key = {"book_get_all", "b_get_all", "b_ga"})
    private void getAllBooks() {
        List<Book> books = bookDao.getAll();
        for (Book book : books) {
            ioService.out(book.toString());
        }
    }

    @ShellMethod(value = "Delete book by id", key = {"book_delete_by_id", "b_delete_by_id", "b_dbi"})
    private void deleteBookById(long id) {
        bookDao.deleteById(id);
        ioService.out(String.format("Book with id = %s deleted", id));
    }
}
