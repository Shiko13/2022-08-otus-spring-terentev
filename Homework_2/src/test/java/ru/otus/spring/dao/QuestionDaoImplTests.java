package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import ru.otus.spring.entity.Question;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class QuestionDaoImplTests {

    @Test
    void shouldHaveCorrectFindAllMethod() {
        QuestionDaoImpl questionDao = new QuestionDaoImpl("quiz.csv");
        List<Question> questionsAndAnswers = questionDao.getAllQuestions();

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
}
