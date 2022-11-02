package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.questions.Question;
import ru.otus.spring.service.QuestionService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");

        QuestionService questionService = context.getBean(QuestionService.class);
        List<Question> questionsAndAnswers = questionService.getAllQuestions();
        for (Question question : questionsAndAnswers) {
            System.out.println(question.getText());
            System.out.println(question.getAnswer());
        }
    }
}
