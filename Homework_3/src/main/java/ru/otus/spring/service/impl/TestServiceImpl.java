package ru.otus.spring.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.AppProps;
import ru.otus.spring.entity.Question;
import ru.otus.spring.entity.User;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.TestService;
import ru.otus.spring.service.UserService;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private final UserService userService;
    private final QuestionService questionService;
    private final IOService ioService;
    private final MessageSource messageSource;
    private final AppProps appProps;

    public TestServiceImpl(UserService userService, QuestionService questionService,
                           IOService ioService, MessageSource messageSource, AppProps appProps) {
        this.userService = userService;
        this.questionService = questionService;
        this.ioService = ioService;
        this.messageSource = messageSource;
        this.appProps = appProps;
    }

    public void run() {
        var questions = questionService.getAllQuestions(appProps.getLocale());
        var user = userService.getUser();
        var resultTestingOfUser = askQuestionsToUser(questions, user);
        ioService.out(resultTestingOfUser);
    }

    public String askQuestionsToUser(List<Question> questions, User user) {
        int count = 0;
        for (Question question : questions) {
            ioService.out(question.getText());
            String answer = ioService.readString();
            if (answer.equals(question.getAnswer())) {
                count++;
            }
        }
        return messageSource.getMessage("test.result", new Object[]{user.getName(),
                user.getSurname(), count}, appProps.getLocale());
    }
}