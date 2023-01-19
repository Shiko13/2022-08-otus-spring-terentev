package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.mongo.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {

}
