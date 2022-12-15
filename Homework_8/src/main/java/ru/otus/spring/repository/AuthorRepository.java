package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

}
