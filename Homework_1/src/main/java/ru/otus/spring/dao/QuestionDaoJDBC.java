package ru.otus.spring.dao;

import com.opencsv.exceptions.CsvRuntimeException;
import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.questions.Question;
import org.springframework.core.io.Resource;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoJDBC implements QuestionDao {
    private final String path;

    public QuestionDaoJDBC(String path) {
        this.path = path;
    }


    @Override
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        Resource resource = new ClassPathResource(path);
        try (FileReader fileReader = new FileReader(resource.getFile());
             CSVReader csvReader = new CSVReader(fileReader)) {
            String[] lines;
            while ((lines = csvReader.readNext()) != null) {
                Question question = new Question();
                question.setText(lines[0]);
                question.setAnswer(lines[1]);
                questions.add(question);
            }
        } catch (IOException | CsvValidationException e) {
            throw new CsvRuntimeException("Problems with reading file");
        }
        return questions;
    }
}
