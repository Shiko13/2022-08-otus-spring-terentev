package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.CommentDao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentDao commentDao;

    @Transactional
    @ShellMethod(value = "Count of comments", key = {"comment_count", "c_count", "c_c"})
    public long getAmountOfComments() {
        return commentDao.count();
    }

    @Transactional
    @ShellMethod(value = "Add new comment", key = {"comment_add", "c_add", "c_a"})
    public String addComment(String text, long bookId) {
        Book book = Book.builder().id(bookId).build();
        Comment comment = Comment.builder()
                .text(text)
                .book(book)
                .build();
        return "id = " + commentDao.insert(comment);
    }

    @Transactional
    @ShellMethod(value = "Get comment by id", key = {"comment_get_by_id", "c_get_by_id", "c_gbi"})
    public String getCommentById(long id) {
        Comment comment = commentDao.getById(id);
        return comment.toString();
    }

    @Transactional
    @ShellMethod(value = "Delete comment by id", key = {"comment_delete_by_id", "c_delete_by_id", "c_dbi"})
    public String deleteCommentById(long id) {
        commentDao.deleteById(id);
        return String.format("Comment with id = %s deleted", id);
    }

    @Transactional
    @ShellMethod(value = "Get comment by id of book", key = {"comment_get_by_book_id", "c_get_by_book_id", "c_gbbi"})
    public String getByBookId(long bookId) {
        List<Comment> comments = commentDao.getByBookId(bookId);
        return comments.toString();
    }
}
