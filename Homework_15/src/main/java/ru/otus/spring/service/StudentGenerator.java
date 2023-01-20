package ru.otus.spring.service;

import ru.otus.spring.domain.Student;

import java.util.List;

public interface StudentGenerator {

    List<Student> generate();
}
