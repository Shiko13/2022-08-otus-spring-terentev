package ru.otus.spring.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.entity.Question;
import ru.otus.spring.service.QuestionService;

import java.util.List;
import java.util.Locale;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Question> getAllQuestions(Locale locale) {
        List<Question> allQuestions = dao.getAllQuestions(locale.getLanguage());
        return allQuestions.stream().toList();
    }
}