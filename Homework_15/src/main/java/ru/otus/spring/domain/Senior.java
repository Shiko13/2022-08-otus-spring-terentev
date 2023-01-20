package ru.otus.spring.domain;

import java.util.UUID;

public class Senior extends ProgrammerEntity {
    public Senior(UUID id, ProgrammerType programmerType, int age, int experience) {
        super(id, programmerType, age, experience);
    }

    @Override
    public String toString() {
        return "Senior{" +
                "id=" + getId() +
                ", programmerType=" + getProgrammerType() +
                ", age=" + getAge() +
                ", experience=" + getExperience() +
                ", isHappy=" + isHappy() +
                '}';
    }
}
