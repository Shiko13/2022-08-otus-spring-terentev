package ru.otus.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.BookShortDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.CommentNotFoundException;
import ru.otus.spring.service.CommentService;

import java.util.List;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/book/{bookId}/comments")
    public String listComments(@PathVariable("bookId") long bookId, Model model) {
        List<CommentDto> comments = commentService.getByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        return "list_comments";
    }

    @PostMapping("/book/{bookId}/comments/delete")
    public String deleteComment(@PathVariable("bookId") long bookId, @RequestParam("id") long id, Model model) {
        commentService.deleteById(id);
        return String.format("redirect:/book/%s/comments", bookId);
    }

    @GetMapping("/book/{bookId}/comments/edit")
    public String editComment(@PathVariable("bookId") long bookId, @RequestParam("id") long id, Model model) {
        CommentDto comment = commentService.getById(id).orElseThrow(() -> new CommentNotFoundException(id));
        model.addAttribute("comment", comment);
        return "edit_comment";
    }

    @PostMapping("/book/{bookId}/comments/edit")
    public String editComment(@PathVariable("bookId") long bookId, @ModelAttribute("comment") CommentDto comment, Model model) {
        commentService.update(comment);
        return String.format("redirect:/book/%s/comments", bookId);
    }

    @GetMapping("/book/{bookId}/comments/create")
    public String createComment(@PathVariable("bookId") long bookId, Model model) {
        model.addAttribute("comment", new CommentDto(null, "", new BookShortDto(bookId)));
        return "edit_comment";
    }

    @PostMapping("/book/{bookId}/comments/create")
    public String createComment(@PathVariable("bookId") long bookId, @ModelAttribute("comment") CommentDto comment, Model model) {
        commentService.insert(comment);
        return String.format("redirect:/book/%s/comments", bookId);
    }
}
