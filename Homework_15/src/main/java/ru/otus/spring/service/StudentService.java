package ru.otus.spring.service;

import ru.otus.spring.domain.Junior;
import ru.otus.spring.domain.Student;

public interface StudentService {

    Junior study(Student student);
}
