package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.entity.Question;
import ru.otus.spring.service.impl.QuestionServiceImpl;
import java.util.List;
import java.util.Locale;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuestionDaoImplTests {

    @Autowired
    private QuestionServiceImpl questionService;

    @Test
    void shouldHaveCorrectFindAllMethodInRussian() {
        List<Question> questionsAndAnswers = questionService.getAllQuestions(new Locale("en"));
        List<String> questions = List.of("What is the capital of Britain?",
                "What is the capital of Germany?",
                "What is the capital of Nepal?",
                "What is the capital of Nigeria?",
                "What is the capital of USA?");

        List<String> answers = List.of("London",
                "Berlin",
                "Kathmandu",
                "Abuja",
                "Washington");

        assertThat(questionsAndAnswers).isNotNull().hasSize(5);
        assertThat(questionsAndAnswers).extracting(Question::getText)
                .containsAll(questions);
        assertThat(questionsAndAnswers).extracting(Question::getAnswer)
                .containsAll(answers);
    }

    @Test
    void shouldHaveCorrectFindAllMethodInEnglish() {
        List<Question> questionsAndAnswers = questionService.getAllQuestions(new Locale("ru"));

        List<String> questions = List.of("Столица Великобритании?",
                "Столица Германии?",
                "Столица Непала?",
                "Столица Нигерии?",
                "Столица США?");

        List<String> answers = List.of("Лондон",
                "Берлин",
                "Катманду",
                "Абуджа",
                "Вашингтон");

        assertThat(questionsAndAnswers).isNotNull().hasSize(5);
        assertThat(questionsAndAnswers).extracting(Question::getText)
                .containsAll(questions);
        assertThat(questionsAndAnswers).extracting(Question::getAnswer)
                .containsAll(answers);
    }
}