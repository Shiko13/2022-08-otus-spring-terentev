package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public ResponseEntity<List<GenreDto>> listBooks() {
        return ResponseEntity.ok(genreService.getAll());
    }
}