package ru.otus.spring.domain;

import java.util.UUID;

public class Student extends ProgrammerEntity {
    public Student(UUID id, ProgrammerType programmerType, int age, int experience) {
        super(id, programmerType, age, experience);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", programmerType=" + getProgrammerType() +
                ", age=" + getAge() +
                ", experience=" + getExperience() +
                ", isHappy=" + isHappy() +
                '}';
    }
}
