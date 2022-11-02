package ru.otus.spring.dao;

import ru.otus.spring.questions.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> getAllQuestions();
}
