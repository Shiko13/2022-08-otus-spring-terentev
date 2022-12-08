package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Import(CommentDaoJpa.class)
public class CommentDaoJpaTest {

    private static final int EXPECTED_COMMENTS_COUNT = 4;
    private static final long EXISTING_COMMENT_ID_1 = 1L;
    private static final long EXISTING_COMMENT_ID_4 = 4L;
    private static final String EXISTING_COMMENT_TEXT_1 = "Amazing book!";
    private static final String EXISTING_COMMENT_TEXT_4 = "Is it a book about big number?";

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TestEntityManager tem;

    @Test
    void shouldReturnExpectedCommentCount() {
        long actualCommentCount = commentDao.count();
        assertThat(actualCommentCount).isEqualTo(EXPECTED_COMMENTS_COUNT);
    }

    @Test
    void shouldInsertComment() {
        Book book = tem.persistAndFlush(Book.builder()
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
                .build());

        Comment firstComment = tem.persistAndFlush(Comment.builder()
                .text("Amazing book!")
                .book(book)
                .build());

        book.setComments(new ArrayList<>());
        book.getComments().add(firstComment);

        Comment expectedComment = tem.persistAndFlush(Comment.builder()
                .text("Is it a book about big number?")
                .book(book)
                .build());

        book.getComments().add(expectedComment);
        commentDao.insert(expectedComment);

        Comment actualComment = tem.find(Comment.class, expectedComment.getId());

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    void shouldReturnExpectedCommentById() {
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

        Comment expectedComment = Comment.builder()
                .id(EXISTING_COMMENT_ID_1)
                .text(EXISTING_COMMENT_TEXT_1)
                .book(book)
                .build();
        book.setComments(new ArrayList<>());
        book.getComments().add(expectedComment);

        Comment actualComment = tem.find(Comment.class, expectedComment.getId());

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    void shouldCorrectDeleteCommentById() {
        assertThatCode(() -> commentDao.getById(EXISTING_COMMENT_ID_1))
                .doesNotThrowAnyException();

        commentDao.deleteById(EXISTING_COMMENT_ID_1);

        assertThat(commentDao.getById(EXISTING_COMMENT_ID_1)).isEqualTo(null);
    }

    @Test
    void shouldReturnListOfCommentsByIdOfBook() {
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

        Comment actualComment1 = commentDao.getById(expectedComment1.getId());
        Comment actualComment2 = commentDao.getById(expectedComment2.getId());
        List<Comment> actualComments = new ArrayList<>();
        actualComments.add(actualComment1);
        actualComments.add(actualComment2);

        assertThat(actualComments).isEqualTo(expectedComments);
    }
}
