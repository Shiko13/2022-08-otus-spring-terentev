package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findById(long id);
    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();
    List<Book> findByAuthor_Id(long authorId);
    void deleteByAuthor_Id(long authorId);
    void deleteByGenre_Id(long genreId);
}
