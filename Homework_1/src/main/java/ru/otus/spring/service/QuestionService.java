package ru.otus.spring.service;

import ru.otus.spring.questions.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions();
}
