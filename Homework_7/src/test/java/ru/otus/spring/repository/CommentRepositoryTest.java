package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TestEntityManager tem;

    private static final long EXISTING_COMMENT_ID_1 = 1L;
    private static final long EXISTING_COMMENT_ID_4 = 4L;
    private static final String EXISTING_COMMENT_TEXT_1 = "Amazing book!";
    private static final String EXISTING_COMMENT_TEXT_4 = "Is it a book about big number?";

    @Test
    void shouldReturnExpectedCommentsByBookId() {
        Book book = Book.builder()
                .id(1L)
                .title("The Silmarillion")
                .publicationYear(1977)
                .author(Author.builder()
                        .id(1L)
                        .name("John")
                        .surname("Tolkien")
                        .build())
                .genre(Genre.builder()
                        .id(3L)
                        .name("Fantasy")
                        .build())
                .build();

        Comment expectedComment1 = Comment.builder()
                .id(EXISTING_COMMENT_ID_1)
                .text(EXISTING_COMMENT_TEXT_1)
                .book(book)
                .build();

        Comment expectedComment2 = Comment.builder()
                .id(EXISTING_COMMENT_ID_4)
                .text(EXISTING_COMMENT_TEXT_4)
                .book(book)
                .build();

        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(expectedComment1);
        expectedComments.add(expectedComment2);
        book.setComments(expectedComments);

        Comment actualComment1 = commentRepository.findById(expectedComment1.getId()).orElse(null);
        Comment actualComment2 = commentRepository.findById(expectedComment2.getId()).orElse(null);
        List<Comment> actualComments = new ArrayList<>();
        actualComments.add(actualComment1);
        actualComments.add(actualComment2);

        assertThat(actualComments).isEqualTo(expectedComments);
    }
}
