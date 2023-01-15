package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.otus.spring.dto.formatter.AuthorFormatter;
import ru.otus.spring.dto.formatter.BookShortFormatter;
import ru.otus.spring.dto.formatter.GenreFormatter;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

    private final AuthorFormatter authorFormatter;
    private final BookShortFormatter bookShortFormatter;
    private final GenreFormatter genreFormatter;
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(authorFormatter);
        registry.addFormatter(bookShortFormatter);
        registry.addFormatter(genreFormatter);
    }
}
