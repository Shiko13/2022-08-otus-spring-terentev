package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Transactional
    @Override
    public long count() {
        return genreDao.count();
    }

    @Transactional
    @Override
    public Genre insert(Genre genre) {
        return genreDao.insert(genre);
    }

    @Transactional
    @Override
    public Genre getById(long id) {
        return genreDao.getById(id);
    }

    @Transactional
    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        genreDao.deleteById(id);
    }
}
