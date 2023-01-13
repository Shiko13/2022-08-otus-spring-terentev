package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.converter.DtoConverter;
import ru.otus.spring.exception.BookNotFoundException;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final DtoConverter<Comment, CommentDto> commentConverter;
    private final MutableAclService mutableAclService;


    @Override
    @Transactional
    public CommentDto insert(CommentDto commentDto) {
        Comment comment = commentConverter.fromDto(commentDto);
        populateBook(comment);
        CommentDto insertedComment = commentConverter.toDto(commentRepository.save(comment));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(insertedComment.getClass(), insertedComment.getId());

        String adminPrincipal = "admin";
        Sid admin = new PrincipalSid(adminPrincipal);
        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.setOwner(owner);
        CumulativePermission cumulativePermissionForAdmin = new CumulativePermission();
        cumulativePermissionForAdmin.set(BasePermission.ADMINISTRATION);
        cumulativePermissionForAdmin.set(BasePermission.CREATE);
        cumulativePermissionForAdmin.set(BasePermission.DELETE);
        cumulativePermissionForAdmin.set(BasePermission.READ);
        cumulativePermissionForAdmin.set(BasePermission.WRITE);
        acl.insertAce(acl.getEntries().size(), cumulativePermissionForAdmin, admin, true);

        if (!adminPrincipal.equals(authentication.getName())) {
            CumulativePermission cumulativePermissionForOwner = new CumulativePermission();
            cumulativePermissionForOwner.set(BasePermission.CREATE);
            cumulativePermissionForOwner.set(BasePermission.DELETE);
            cumulativePermissionForOwner.set(BasePermission.READ);
            cumulativePermissionForOwner.set(BasePermission.WRITE);
            acl.insertAce(acl.getEntries().size(), cumulativePermissionForOwner, owner, true);
        }
        mutableAclService.updateAcl(acl);

        return insertedComment;
    }


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#commentDto.id, 'ru.otus.spring.dto.CommentDto', 'WRITE')")
    public CommentDto update(CommentDto commentDto) {
        Comment comment = commentConverter.fromDto(commentDto);
        populateBook(comment);
        return commentConverter.toDto(commentRepository.save(comment));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.spring.dto.CommentDto', 'DELETE')")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }


    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.spring.dto.CommentDto', 'READ')")
    public Optional<CommentDto> getById(long id) {
        return commentRepository.findById(id).map(commentConverter::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<CommentDto> getByBookId(long bookId) {
        Book book = getBook(bookId);
        if (book.getComments() == null) {
            return List.of();
        }
        return getBook(bookId).getComments().stream()
                .map(commentConverter::toDto)
                .collect(Collectors.toList());
    }

    private void populateBook(Comment comment) {
        comment.setBook(getCommentBook(comment));
    }

    private Book getCommentBook(Comment comment) {
        return getBook(comment.getBook().getId());
    }

    private Book getBook(long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
        return book.get();
    }
}
