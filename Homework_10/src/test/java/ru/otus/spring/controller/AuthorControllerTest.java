package ru.otus.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    private static final Author EXPECTED_AUTHOR = new Author(1L, "John Tolkien");
    private static final Author EXPECTED_AUTHOR2 = new Author(2L, "Lev Tolstoy");
    private static final AuthorDto EXPECTED_AUTHOR_DTO = new AuthorDto(EXPECTED_AUTHOR.getId(), EXPECTED_AUTHOR.getName());
    private static final AuthorDto EXPECTED_AUTHOR_DTO2 = new AuthorDto(EXPECTED_AUTHOR2.getId(), EXPECTED_AUTHOR2.getName());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        when(authorService.getAll()).thenReturn(List.of(EXPECTED_AUTHOR_DTO, EXPECTED_AUTHOR_DTO2));
    }

    @Test
    void shouldReturnExpectedAuthors() throws Exception {
        List<AuthorDto> expected = List.of(EXPECTED_AUTHOR_DTO, EXPECTED_AUTHOR_DTO2);

        mvc.perform(get("/api/authors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }
}
