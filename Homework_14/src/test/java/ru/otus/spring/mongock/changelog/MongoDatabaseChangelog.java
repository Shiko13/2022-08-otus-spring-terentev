package ru.otus.spring.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.domain.mongo.Author;
import ru.otus.spring.domain.mongo.Book;
import ru.otus.spring.domain.mongo.Comment;
import ru.otus.spring.domain.mongo.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;

@ChangeLog
public class MongoDatabaseChangelog {

    public static Author AUTHOR_1;
    public static Author AUTHOR_2;
    public static Author AUTHOR_3;
    public static Genre GENRE_1;
    public static Genre GENRE_2;
    public static Genre GENRE_3;
    public static Comment COMMENT_1;
    public static Comment COMMENT_2;
    public static Comment COMMENT_3;
    public static Comment COMMENT_4;
    public static Comment COMMENT_5;
    public static Book BOOK_1;
    public static Book BOOK_2;
    public static Book BOOK_3;

    @ChangeSet(order = "001", id = "dropDb", author = "sergey", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "sergey")
    public void initAuthors(AuthorRepository authorRepository) {
        AUTHOR_1 = authorRepository.save(new Author(null, "John Tolkien"));
        AUTHOR_2 = authorRepository.save(new Author(null, "Lev Tolstoy"));
        AUTHOR_3 = authorRepository.save(new Author(null, "Miguel Cervantes"));
    }

    @ChangeSet(order = "003", id = "initGenres", author = "sergey")
    public void initGenres(GenreRepository genreRepository) {
        GENRE_1 = genreRepository.save(new Genre(null, "Novel"));
        GENRE_2 = genreRepository.save(new Genre(null, "Novella"));
        GENRE_3 = genreRepository.save(new Genre(null, "Fantasy"));
    }

    @ChangeSet(order = "004", id = "initComments", author = "sergey")
    public void initComments(CommentRepository commentRepository) {
        COMMENT_1 = commentRepository.save(new Comment(null, "Amazing book!"));
        COMMENT_2 = commentRepository.save(new Comment(null, "Is it a book about big number?"));
        COMMENT_3 = commentRepository.save(new Comment(null, "Is it book about music?"));
        COMMENT_4 = commentRepository.save(new Comment(null, "Sancho Panza is my crush ^_^"));
        COMMENT_5 = commentRepository.save(new Comment(null, "Dulcinea del Toboso is not princess"));
    }

    @ChangeSet(order = "005", id = "initBooks", author = "sergey")
    public void initBooks(BookRepository bookRepository) {
        BOOK_1 = bookRepository.save(new Book(null, "The Silmarillion", AUTHOR_1,
                List.of(GENRE_3), List.of(COMMENT_1, COMMENT_2)));
        BOOK_2 = bookRepository.save(new Book(null, "The Kreutzer Sonata", AUTHOR_2,
                List.of(GENRE_2), List.of(COMMENT_3)));
        BOOK_3 = bookRepository.save(new Book(null, "The Ingenious Gentleman Don Quixote of La Mancha",
                AUTHOR_3, List.of(GENRE_1), List.of(COMMENT_4, COMMENT_5)));
    }
}