package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByBook_Id(long bookId);
}
