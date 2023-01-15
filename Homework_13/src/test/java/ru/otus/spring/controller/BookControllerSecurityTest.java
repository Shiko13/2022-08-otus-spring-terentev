package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.config.SecurityConfig;
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

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
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
            .name("Lev Tolstoy")
            .build();

    private static final GenreDto EXPECTED_GENRE = GenreDto.builder()
            .id(1L)
            .name("Novel")
            .build();
    private static final BookDto book = new BookDto(4L, "Resurrection",
            EXPECTED_AUTHOR, EXPECTED_GENRE);

    private static final BookDto editBook = new BookDto(2L, "The Kreutzer Sonata-2",
            EXPECTED_AUTHOR, EXPECTED_GENRE);

    private static final AuthorDto EXPECTED_AUTHOR_1 = AuthorDto.builder()
            .id(1L)
            .name("John Tolkien")
            .build();

    private static final GenreDto EXPECTED_GENRE_3 = GenreDto.builder()
            .id(3L)
            .name("Fantasy")
            .build();
    private static final BookDto EXPECTED_BOOK_DTO_1 = new BookDto(1L, "The Silmarillion",
            EXPECTED_AUTHOR_1, EXPECTED_GENRE_3);

    @BeforeEach
    public void setUp() {
        when(bookService.getById(EXPECTED_BOOK_DTO_1.getId())).thenReturn(Optional.of(EXPECTED_BOOK_DTO_1));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void editShouldBeAvailableForAdmin() throws Exception {
        mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void editShouldNotBeNotAvailableForUser() throws Exception {
        mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "anonymous", roles = "ANONYMOUS")
    public void editShouldNotBeNotAvailableForAnonymous() throws Exception {
        mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addingShouldBeAvailableForAdmin() throws Exception {
        mvc.perform(get("/book/create"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void addingShouldBeNotAvailableForUser() throws Exception {
        mvc.perform(get("/book/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "anonymous", roles = "ANONYMOUS")
    public void addingShouldBeNotAvailableForAnonymous() throws Exception {
        mvc.perform(get("/book/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deletingShouldBeAvailableForAdmin() throws Exception {
        mvc.perform(post("/book/delete?id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void deletingShouldBeNotAvailableForUser() throws Exception {
        mvc.perform(post("/book/delete?id=1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "anonymous", roles = "ANONYMOUS")
    public void deletingShouldBeNotAvailableForAnonymous() throws Exception {
        mvc.perform(post("/book/delete?id=1"))
                .andExpect(status().isForbidden());
    }
}