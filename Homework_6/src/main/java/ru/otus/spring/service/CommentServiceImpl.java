package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.CommentDao;
import ru.otus.spring.domain.Comment;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    @Override
    public long count() {
        return commentDao.count();
    }

    @Override
    public long insert(Comment comment) {
        return commentDao.insert(comment);
    }

    @Override
    public Comment getById(long id) {
        return commentDao.getById(id);
    }

    @Override
    public List<Comment> getAll() {
        return commentDao.getAll();
    }

    @Override
    public void deleteById(long id) {
        commentDao.deleteById(id);
    }
}
