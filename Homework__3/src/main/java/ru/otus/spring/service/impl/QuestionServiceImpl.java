package ru.otus.spring.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.AppProps;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.entity.Question;
import ru.otus.spring.service.QuestionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao dao;
    private final MessageSource messageSource;
    private final AppProps appProps;

    public QuestionServiceImpl(QuestionDao dao, MessageSource messageSource, AppProps appProps) {
        this.dao = dao;
        this.messageSource = messageSource;
        this.appProps = appProps;
    }

    @Override
    public List<Question> getAllQuestions(Locale locale) {
        List<Question> allQuestions = dao.getAllQuestions();
        if (locale.getLanguage().equals("en")) {
            return allQuestions.stream().limit(5).toList();
        } else {
            return allQuestions.stream().skip(5).toList();
        }
    }
}