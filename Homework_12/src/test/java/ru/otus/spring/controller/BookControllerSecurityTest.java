package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.formatter.AuthorFormatter;
import ru.otus.spring.dto.formatter.BookShortFormatter;
import ru.otus.spring.dto.formatter.GenreFormatter;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({BookController.class, AuthorFormatter.class, BookShortFormatter.class, GenreFormatter.class})
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private UserService userService;

    private static final AuthorDto EXPECTED_AUTHOR = AuthorDto.builder()
            .id(2L)
            .name("Lev")
            .surname("Tolstoy")
            .build();

    private static final GenreDto EXPECTED_GENRE = GenreDto.builder()
            .id(1L)
            .name("Novel")
            .build();
    private static final BookDto book = new BookDto(4L, "Resurrection",
            1899, EXPECTED_AUTHOR, EXPECTED_GENRE);

    private static final BookDto editBook = new BookDto(2L, "The Kreutzer Sonata-2",
            1889, EXPECTED_AUTHOR, EXPECTED_GENRE);


    @Test
    @WithMockUser(username = "login")
    public void allBooksPageShouldBeAvailableForAuthorizedUser() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/list"));
    }

    @Test
    public void allBooksPageShouldNotBeAvailableForUnauthorizedUser() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "login")
    public void editBooksPageShouldBeAvailableForAuthorizedUser() throws Exception {
        mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void editBooksPageShouldNotBeAvailableForUnauthorizedUser() throws Exception {
        mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "login")
    public void editingBookShouldBeAvailableForAuthorizedUser() throws Exception {
        mvc.perform(post("/book/edit?id=1", editBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book"));
    }

    @Test
    public void editingBookShouldNotBeAvailableForUnauthorizedUser() throws Exception {
        mvc.perform(post("/book/edit?id=1", editBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "login")
    public void addBookPageShouldBeAvailableForAuthorizedUser() throws Exception {
        mvc.perform(get("/book/add"))
                .andExpect(status().isOk());
    }

    @Test
    public void addBookPageShouldNotBeAvailableForUnauthorizedUser() throws Exception {
        mvc.perform(get("/book/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "login")
    public void savingBookShouldBeAvailableForAuthorizedUser() throws Exception {
        mvc.perform(post("/book/add", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book"));
    }

    @Test
    public void deletingBookShouldNotBeAvailableForUnauthorizedUser() throws Exception {
        mvc.perform(post("/book/add", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "login")
    public void deletingBookShouldBeAvailableForAuthorizedUser() throws Exception {
        mvc.perform(post("/book/remove?id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book"));
    }

    @Test
    public void savingBookShouldNotBeAvailableForUnauthorizedUser() throws Exception {
        mvc.perform(post("/book/remove?id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}