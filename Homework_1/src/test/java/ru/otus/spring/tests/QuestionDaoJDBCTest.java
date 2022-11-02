package ru.otus.spring.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.questions.Question;
import ru.otus.spring.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

class QuestionDaoJDBCTest {

    ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("/spring-context.xml");
    QuestionService questionService = context.getBean(QuestionService.class);
    List<Question> questions = questionService.getAllQuestions();

    @Test
    void answersShouldBeEquals() {
        List<String> rightAnswers = List.of(
            "London",
                "Berlin",
                "Kathmandu",
                "Abuja",
                "Washington"
        );

        List<String> answers = new ArrayList<>();
        for (Question question : questions) {
            answers.add(question.getAnswer());
        }
        assertEquals(rightAnswers, answers);
    }
}