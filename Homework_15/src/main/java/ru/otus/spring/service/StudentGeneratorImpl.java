package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.ProgrammerType;
import ru.otus.spring.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.concurrent.ThreadLocalRandom.current;

@Service
public class StudentGeneratorImpl implements StudentGenerator {
    @Override
    public List<Student> generate() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < current().nextInt(1, 10); i++) {
            students.add(generateStudent());
        }
        return students;
    }

    private Student generateStudent() {
        return new Student(UUID.randomUUID(),
                ProgrammerType.values()[current().nextInt(0, ProgrammerType.values().length)],
                17, 0);
    }
}
