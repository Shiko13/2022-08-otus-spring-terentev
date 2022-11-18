package ru.otus.spring.service;

import ru.otus.spring.entity.Question;

import java.util.List;
import java.util.Locale;

public interface QuestionService {
    List<Question> getAllQuestions(Locale locale);
}


