package ru.otus.spring.dto;

public record BookShortDto(Long id) {
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                '}';
    }
}
