package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.domain.Genre;

import java.util.Optional;

@RepositoryRestResource(path = "genres")
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @RestResource(path = "name", rel = "name")
    Optional<Genre> findByName(String name);
}
