package ru.otus.spring.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvRuntimeException;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.spring.entity.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionDaoImpl implements QuestionDao {
    private final String path;

    public QuestionDaoImpl(@Value("${path}") String path) {
        this.path = path;
    }

    @Override
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();

        try (InputStream is = new ClassPathResource(path).getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is));
             CSVReader csvReader = new CSVReader(br)) {
            String[] lines;
            while ((lines = csvReader.readNext()) != null) {
                Question question = new Question();
                question.setText(lines[0]);
                question.setAnswer(lines[1]);
                questions.add(question);
            }
        } catch (IOException | CsvValidationException | ArrayIndexOutOfBoundsException e) {
            throw new CsvRuntimeException("Problem with reading csv", e);
        }
        return questions;
    }
}
