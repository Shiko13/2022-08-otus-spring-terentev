package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.service.CommentService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    @ShellMethod(value = "Count of comments", key = {"comment_count", "c_count", "c_c"})
    public long getAmountOfComments() {
        return commentService.count();
    }

    @ShellMethod(value = "Add new comment", key = {"comment_add", "c_add", "c_a"})
    public String addComment(String text, String bookId) {
        Book book = Book.builder().id(bookId).build();
        Comment comment = new Comment(text, book);
        return "Comment = " + commentService.insert(comment);
    }

    @ShellMethod(value = "Get comment by id", key = {"comment_get_by_id", "c_get_by_id", "c_gbi"})
    public String getCommentById(String id) {
        Comment comment = commentService.getById(id);
        return comment.toString();
    }

    @ShellMethod(value = "Delete comment by id", key = {"comment_delete_by_id", "c_delete_by_id", "c_dbi"})
    public String deleteCommentById(String id) {
        commentService.deleteById(id);
        return String.format("Comment with id = %s deleted", id);
    }

    @ShellMethod(value = "Get comment by id of book", key = {"comment_get_by_book_id", "c_get_by_book_id", "c_gbbi"})
    public String getByBookId(String bookId) {
        List<CommentDto> comments = commentService.getByBookId(bookId);
        return comments.toString();
    }
}
