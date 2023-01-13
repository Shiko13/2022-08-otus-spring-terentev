package ru.otus.spring.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.spring.config.SecurityConfig;
import ru.otus.spring.dto.formatter.AuthorFormatter;
import ru.otus.spring.dto.formatter.BookShortFormatter;
import ru.otus.spring.dto.formatter.GenreFormatter;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.UserService;

import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest({CommentController.class, AuthorFormatter.class, BookShortFormatter.class, GenreFormatter.class})
public class CommentSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserService userService;


    @ParameterizedTest
    @WithMockUser(username = "anonymous", authorities = {"ANONYMOUS"})
    @CsvSource({"/book/1/comment,GET", "/book/1/comment/edit,GET", "/book/1/comments/create,GET",
            "/book/1/comments/delete,POST", "/book/1/comments/edit,POST", "/book/1/comments/create,POST"})
    void shouldForbidAllCommentPagesForAnonymous(String url, String method) throws Exception {
        mvc.perform(MockMvcRequestBuilders.request(Objects.requireNonNull(HttpMethod.resolve(method)), url))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource({"/book/1/comments,GET", "/book/1/comments/edit,GET", "/book/1/comments/create,GET",
            "/book/1/comments/delete,POST", "/book/1/comments/edit,POST", "/book/1/comments/create,POST"})
    void shouldRedirectToLoginAllCommentPagesWithoutAuthorization(String url, String method) throws Exception {
        mvc.perform(MockMvcRequestBuilders.request(Objects.requireNonNull(HttpMethod.resolve(method)), url))
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
