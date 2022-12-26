package ru.otus.spring.dto;

public class BookDtoOnlyId {

    private final Long id;

    public BookDtoOnlyId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            '}';
    }

}
