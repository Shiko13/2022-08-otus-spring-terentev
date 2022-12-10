package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Transactional
    @Override
    public long count() {
        return authorDao.count();
    }

    @Transactional
    @Override
    public Author insert(Author author) {
        return authorDao.insert(author);
    }

    @Transactional
    @Override
    public Author getById(long id) {
        return authorDao.getById(id);
    }

    @Transactional
    @Override
    public List<Author> getAll() {
        return authorDao.getAll();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        authorDao.deleteById(id);
    }
}
