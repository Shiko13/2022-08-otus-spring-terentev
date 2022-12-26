package ru.otus.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.otus.spring.dto.formatter.AuthorFormatter;
import ru.otus.spring.dto.formatter.BookShortFormatter;
import ru.otus.spring.dto.formatter.GenreFormatter;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new GenreFormatter());
        registry.addFormatter(new AuthorFormatter());
        registry.addFormatter(new BookShortFormatter());
    }
}