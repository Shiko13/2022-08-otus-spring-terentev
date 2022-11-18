package ru.otus.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private final String name;
    private final String surname;
}
