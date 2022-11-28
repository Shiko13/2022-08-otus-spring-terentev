package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Genre {
    private final long id;
    private final String name;
}
