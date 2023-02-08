package ru.otus.spring.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private final List<Author> authors = new ArrayList<>();
    private final List<Genre> genres = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();

    @ChangeSet(order = "001", id = "dropDb", author = "sergey", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "sergey")
    public void initAuthors(AuthorRepository authorRepository) {
        authors.add(authorRepository.save(new Author(null, "John Tolkien")).block());
        authors.add(authorRepository.save(new Author(null, "Lev Tolstoy")).block());
        authors.add(authorRepository.save(new Author(null, "Miguel Cervantes")).block());
    }

    @ChangeSet(order = "003", id = "initGenres", author = "sergey")
    public void initGenres(GenreRepository genreRepository) {
        genres.add(genreRepository.save(new Genre(null, "Novel")).block());
        genres.add(genreRepository.save(new Genre(null, "Novella")).block());
        genres.add(genreRepository.save(new Genre(null, "Fantasy")).block());
        genres.add(genreRepository.save(new Genre(null, "Poem")).block());
        genres.add(genreRepository.save(new Genre(null, "Short story")).block());
    }

    @ChangeSet(order = "004", id = "initComments", author = "sergey")
    public void initComments(CommentRepository commentRepository) {
        comments.add(commentRepository.save(new Comment(null, "Amazing book!")).block());
        comments.add(commentRepository.save(new Comment(null, "Is it a book about big number?")).block());
        comments.add(commentRepository.save(new Comment(null, "Is it book about music?")).block());
        comments.add(commentRepository.save(new Comment(null, "Sancho Panza is my crush ^_^")).block());
        comments.add(commentRepository.save(new Comment(null, "Dulcinea del Toboso is not princess")).block());
        comments.add(commentRepository.save(new Comment(null, "Is it book about Prometheus?")).block());
        comments.add(commentRepository.save(new Comment(null, "Poem is not finished")).block());
        comments.add(commentRepository.save(new Comment(null, "Poem published in 2013")).block());
    }

    @ChangeSet(order = "005", id = "initBooks", author = "sergey")
    public void initBooks(BookRepository bookRepository) {
        books.add(bookRepository.save(new Book(null, "The Silmarillion", authors.get(0), genres.get(2),
                List.of(comments.get(0), comments.get(1)))).block());
        books.add(bookRepository.save(new Book(null, "The Kreutzer Sonata", authors.get(1), genres.get(1),
                List.of(comments.get(2)))).block());
        books.add(bookRepository.save(new Book(null, "The Ingenious Gentleman Don Quixote of La Mancha",
                authors.get(2), genres.get(0), List.of(comments.get(3), comments.get(4)))).block());
        books.add(bookRepository.save(new Book(null, "The Prisoner of the Caucasus", authors.get(1),
                genres.get(4), List.of(comments.get(5)))).block());
        books.add(bookRepository.save(new Book(null, "The Fall of Arthur", authors.get(0),
                genres.get(3), List.of(comments.get(6), comments.get(7)))).block());
    }
}
