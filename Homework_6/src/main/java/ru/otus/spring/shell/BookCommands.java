package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @ShellMethod(value = "Count of books", key = {"book_count", "b_count", "b_c"})
    public long getAmountOfBooks() {
        return bookDao.count();
    }

    @Transactional
    @ShellMethod(value = "Add new book", key = {"book_add", "b_add", "b_a"})
    public String addBook(String title, int publicationYear, long authorId, long genreId) {
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

    @Transactional
    @ShellMethod(value = "Get book by id", key = {"book_get_by_id", "b_get_by_id", "b_gbi"})
    public String getBookById(long id) {
        Book book = bookDao.getById(id);
        return book.toString();
    }

    @Transactional
    @ShellMethod(value = "Get all books", key = {"book_get_all", "b_get_all", "b_ga"})
    public List<String> getAllBooks() {
        List<Book> books = bookDao.getAll();
        List<String> booksToString = new ArrayList<>();
        for (Book book : books) {
            booksToString.add(book.toString());
        }
        return booksToString;
    }

    @Transactional
    @ShellMethod(value = "Delete book by id", key = {"book_delete_by_id", "b_delete_by_id", "b_dbi"})
    public String deleteBookById(long id) {
        bookDao.deleteById(id);
        return String.format("Book with id = %s deleted", id);
    }
}