package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.CommentDao;
import ru.otus.spring.domain.Comment;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    @Transactional
    @Override
    public long count() {
        return commentDao.count();
    }

    @Transactional
    @Override
    public Comment insert(Comment comment) {
        return commentDao.insert(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getById(long id) {
        return commentDao.getById(id);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentDao.deleteById(id);
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    @Override
    public List<Comment> getByBookId(long bookId) {
        return commentDao.getByBookId(bookId);
    }
}
