package ru.otus.spring.domain;

import java.util.UUID;

public class Middle extends ProgrammerEntity {
    public Middle(UUID id, ProgrammerType programmerType, int age, int experience) {
        super(id, programmerType, age, experience);
    }

    @Override
    public String toString() {
        return "Middle{" +
                "id=" + getId() +
                ", programmerType=" + getProgrammerType() +
                ", age=" + getAge() +
                ", experience=" + getExperience() +
                ", isHappy=" + isHappy() +
                '}';
    }
}
