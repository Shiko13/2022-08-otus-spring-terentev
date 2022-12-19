package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByAuthor_Id(String authorId);
    void deleteByAuthor_Id(String authorId);
    void deleteByGenre_Id(String genreId);
}
