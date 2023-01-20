package ru.otus.spring.domain;

import lombok.Data;
import java.util.UUID;

@Data
public abstract class ProgrammerEntity {

    private final UUID id;
    private final ProgrammerType programmerType;
    private int age;
    private int experience;
    private boolean isHappy;

    public ProgrammerEntity(UUID id, ProgrammerType programmerType, int age, int experience) {
        this.id = id;
        this.programmerType = programmerType;
        this.age = age;
        this.experience = experience;
        this.isHappy = false;
    }
}
