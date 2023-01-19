package ru.otus.spring.domain;

import java.util.UUID;

public class Junior extends ProgrammerEntity {
    public Junior(UUID id, ProgrammerType programmerType, int age, int experience) {
        super(id, programmerType, age, experience);
    }

    @Override
    public String toString() {
        return "Junior{" +
                "id=" + getId() +
                ", programmerType=" + getProgrammerType() +
                ", age=" + getAge() +
                ", experience=" + getExperience() +
                ", isHappy=" + isHappy() +
                '}';
    }
}
