package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.CommentDao;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.converter.DtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final DtoConverter<Comment, CommentDto> commentConverter;

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

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getByBookId(long bookId) {
        return commentDao.getByBookId(bookId)
                .stream()
                .map(commentConverter::toDto)
                .collect(Collectors.toList());
    }
}
