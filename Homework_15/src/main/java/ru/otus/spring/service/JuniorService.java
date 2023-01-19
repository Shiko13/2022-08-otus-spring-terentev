package ru.otus.spring.service;

import ru.otus.spring.domain.Junior;
import ru.otus.spring.domain.Middle;

public interface JuniorService {

    Middle job(Junior junior);
}
