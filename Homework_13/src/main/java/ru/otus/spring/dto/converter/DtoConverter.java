package ru.otus.spring.dto.converter;

public interface DtoConverter<E, T> {

    T toDto(E entity);

    E fromDto(T dto);
}
