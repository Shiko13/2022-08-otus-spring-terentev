package ru.otus.spring.dao;

import ru.otus.spring.entity.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> getAllQuestions();
}
