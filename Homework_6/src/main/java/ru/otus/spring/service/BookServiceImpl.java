package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Transactional
    @Override
    public long count() {
        return bookDao.count();
    }

    @Transactional
    @Override
    public Book insert(Book book) {
        return bookDao.insert(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> getById(long id) {
        return bookDao.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookDao.deleteById(id);
    }
}
