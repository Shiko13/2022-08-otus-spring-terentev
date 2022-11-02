package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.entity.Question;
import ru.otus.spring.entity.User;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private final UserService userService;
    private final QuestionService questionService;
    private final IOService ioService;

    public TestServiceImpl(UserService userService, QuestionService questionService, IOService ioService) {
        this.userService = userService;
        this.questionService = questionService;
        this.ioService = ioService;
    }

    public void run() {
        var questions = questionService.getAllQuestions();
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
        return "Dear " + user.getName() +
                " " + user.getSurname() + ", your result: "
                + count + " correct answer(s)";
    }
}