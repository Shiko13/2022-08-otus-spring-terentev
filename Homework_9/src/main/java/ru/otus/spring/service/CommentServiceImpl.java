package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final DtoConverter<Comment, CommentDto> commentConverter;

    @Transactional
    @Override
    public long count() {
        return commentRepository.count();
    }

    @Transactional
    @Override
    public Comment insert(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getByBookId(long bookId) {
        return commentRepository.findByBook_Id(bookId)
                .stream()
                .map(commentConverter::toDto)
                .collect(Collectors.toList());
    }
}
