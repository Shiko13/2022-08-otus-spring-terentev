package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String EXISTING_COMMENT_TEXT_1 = "Amazing book!";
    private static final String EXISTING_COMMENT_TEXT_4 = "Is it a book about big number?";

    @Test
    void shouldReturnExpectedCommentsByBookId() {
        Query query = new Query().addCriteria(Criteria.where("title").is("The Silmarillion"));
        Book book = mongoTemplate.findOne(query, Book.class);

        Comment expectedComment1 = new Comment(EXISTING_COMMENT_TEXT_1, book);
        Comment expectedComment2 = new Comment(EXISTING_COMMENT_TEXT_4, book);

        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(expectedComment1);
        expectedComments.add(expectedComment2);
        Objects.requireNonNull(book).setComments(expectedComments);

        List<Comment> actualComments = commentRepository.findByBook_Id(book.getId());

        assertThat(actualComments.get(0).getText()).isEqualTo(expectedComments.get(0).getText());
        assertThat(actualComments.get(0).getBook()).isEqualTo(expectedComments.get(0).getBook());
        assertThat(actualComments.get(1).getText()).isEqualTo(expectedComments.get(1).getText());
        assertThat(actualComments.get(1).getBook()).isEqualTo(expectedComments.get(1).getBook());
    }
}