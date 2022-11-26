package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookDao bookDao;

    @ShellMethod(value = "Count of books", key = {"book_count", "b_count", "b_c"})
    private long getAmountOfBooks() {
        return bookDao.count();
    }

    @ShellMethod(value = "Add new book", key = {"book_add", "b_add", "b_a"})
    private String addBook(String title, int publicationYear, long authorId, long genreId) {
        Author author = Author.builder().id(authorId).build();
        Genre genre = Genre.builder().id(genreId).build();
        Book book = Book.builder()
                        .title(title)
                        .publicationYear(publicationYear)
                        .author(author)
                        .genre(genre)
                        .build();
        return "id = " + bookDao.insert(book);
    }

    @ShellMethod(value = "Get book by id", key = {"book_get_by_id", "b_get_by_id", "b_gbi"})
    private String getBookById(long id) {
        Book book = bookDao.getById(id);
        return book.toString();
    }

    @ShellMethod(value = "Get all books", key = {"book_get_all", "b_get_all", "b_ga"})
    private List<String> getAllBooks() {
        List<Book> books = bookDao.getAll();
        List<String> booksToString = new ArrayList<>();
        for (Book book : books) {
            booksToString.add(book.toString());
        }
        return booksToString;
    }

    @ShellMethod(value = "Delete book by id", key = {"book_delete_by_id", "b_delete_by_id", "b_dbi"})
    private String deleteBookById(long id) {
        bookDao.deleteById(id);
        return String.format("Book with id = %s deleted", id);
    }
}