package ru.otus.spring.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "sergei", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "sergei", runAlways = true)
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authors = db.getCollection("authors");
        authors.insertMany(Arrays.asList(
                new Document().append("name", "John").append("surname", "Tolkien"),
                new Document().append("name", "Lev").append("surname", "Tolstoy"),
                new Document().append("name", "Miguel").append("surname", "Cervantes")));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "sergei", runAlways = true)
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genres = db.getCollection("genres");
        genres.insertMany(Arrays.asList(
                new Document().append("name", "Novel"),
                new Document().append("name", "Novella"),
                new Document().append("name", "Fantasy")));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "sergei", runAlways = true)
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> books = db.getCollection("books"),
                authors = db.getCollection("authors"),
                genres = db.getCollection("genres");
        books.insertMany(Arrays.asList(
                new Document().append("title", "The Silmarillion")
                        .append("publicationYear", 1977)
                        .append("author", getAuthorId(authors, "Tolkien"))
                        .append("genre", getGenreId(genres, "Fantasy")),
                new Document().append("title", "The Kreutzer Sonata")
                        .append("publicationYear", 1889)
                        .append("author", getAuthorId(authors, "Tolstoy"))
                        .append("genre", getGenreId(genres, "Novella")),
                new Document().append("title", "The Ingenious Gentleman Don Quixote of La Mancha")
                        .append("publicationYear", 1605)
                        .append("author", getAuthorId(authors, "Cervantes"))
                        .append("genre", getGenreId(genres, "Novel"))));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "sergei", runAlways = true)
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> books = db.getCollection("books"),
                comments = db.getCollection("comments");
        comments.insertMany(Arrays.asList(
                new Document().append("text", "Amazing book!")
                        .append("book", getBookId(books, "The Silmarillion")),
                new Document().append("text", "Is it book about music?")
                        .append("book", getBookId(books, "The Kreutzer Sonata")),
                new Document().append("text", "Sancho Panza is my crush ^_^")
                        .append("book", getBookId(books, "The Ingenious Gentleman Don Quixote of La Mancha")),
                new Document().append("text", "Is it a book about big number?")
                        .append("book", getBookId(books, "The Silmarillion"))));
    }

    private Object getAuthorId(MongoCollection<Document> collection, String surname) {
        return Objects.requireNonNull(collection.find(eq("surname", surname)).first()).get("_id");
    }

    private Object getGenreId(MongoCollection<Document> collection, String name) {
        return Objects.requireNonNull(collection.find(eq("name", name)).first()).get("_id");
    }

    private Object getBookId(MongoCollection<Document> collection, String title) {
        return Objects.requireNonNull(collection.find(eq("title", title)).first()).get("_id");
    }
}
