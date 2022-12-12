package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.id = :id")
    Optional<Book> findById(@Param("id") long id);

    @Query("select distinct b from Book b join fetch b.author join fetch b.genre")
    List<Book> findAll();
}
