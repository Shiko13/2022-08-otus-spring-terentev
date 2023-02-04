package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.exception.CommentNotFoundException;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
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
    public CommentDto insert(CommentDto commentDto) {
        Comment comment = commentConverter.fromDto(commentDto);
        return commentConverter.toDto(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> getById(long id) {
        return commentRepository.findById(id).map(commentConverter::toDto);
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

    @Override
    public CommentDto update(CommentDto commentDto) {
        Comment comment = commentConverter.fromDto(commentDto);
        if (comment.getId() == null || commentRepository.findById(commentDto.getId()).isEmpty()) {
            throw new CommentNotFoundException(comment.getId());
        }
        return commentConverter.toDto(commentRepository.save(comment));
    }
}
