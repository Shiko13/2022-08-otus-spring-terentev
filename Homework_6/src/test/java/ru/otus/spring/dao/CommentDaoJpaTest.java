package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

    private static final int EXPECTED_COMMENTS_COUNT = 3;
    private static final long EXISTING_COMMENT_ID_1 = 1L;
    private static final String EXISTING_COMMENT_TEXT_1 = "Amazing book!";
    private static final long EXISTING_COMMENT_ID_2 = 2L;
    private static final String EXISTING_COMMENT_TEXT_2 = "Is it book about music?";
    private static final long EXISTING_COMMENT_ID_3 = 3L;
    private static final String EXISTING_COMMENT_TEXT_3 = "Sancho Panza is my crush ^_^";

    @Autowired
    private CommentDao commentDao;

    @Test
    void shouldReturnExpectedCommentCount() {
        long actualCommentCount = commentDao.count();
        assertThat(actualCommentCount).isEqualTo(EXPECTED_COMMENTS_COUNT);
    }

    @Test
    void shouldInsertComment() {
        long expectedId = 4L;
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
        Comment firstComment = Comment.builder()
                .id(1L)
                .text("Amazing book!")
                .book(book)
                .build();
        book.setComments(new ArrayList<>());
        book.getComments().add(firstComment);

        Comment expectedComment = Comment.builder()
                .id(expectedId)
                .text("Is it a book about big number?")
                .book(book)
                .build();
        book.getComments().add(expectedComment);

        commentDao.insert(expectedComment);
        Comment actualComment = commentDao.getById(expectedId);
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
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

        Comment actualComment = commentDao.getById(expectedComment.getId());
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    void shouldCorrectDeleteCommentById() {
        assertThatCode(() -> commentDao.getById(EXISTING_COMMENT_ID_1))
                .doesNotThrowAnyException();

        commentDao.deleteById(EXISTING_COMMENT_ID_1);
        assertThat(commentDao.getById(EXISTING_COMMENT_ID_1)).isEqualTo(null);
    }

    @Test
    void shouldReturnExpectedCommentList() {
        Comment comment1 = Comment.builder()
                .id(EXISTING_COMMENT_ID_1)
                .text(EXISTING_COMMENT_TEXT_1)
                .book(Book.builder()
                        .id(1L)
                        .build())
                .build();
        Comment comment2 = Comment.builder()
                .id(EXISTING_COMMENT_ID_2)
                .text(EXISTING_COMMENT_TEXT_2)
                .book(Book.builder()
                        .id(2L)
                        .build())
                .build();
        Comment comment3 = Comment.builder()
                .id(EXISTING_COMMENT_ID_3)
                .text(EXISTING_COMMENT_TEXT_3)
                .book(Book.builder()
                        .id(3L)
                        .build())
                .build();

        List<Comment> actualCommentList = commentDao.getAll();
        assertThat(actualCommentList)
                .containsExactlyInAnyOrder(comment1, comment2, comment3);
    }
}
