package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentDaoJpa implements CommentDao {

    private final EntityManager em;

    @Override
    public long count() {
        TypedQuery<Long> query = em.createQuery("select count(c.id) from Comment c", Long.class);
        return query.getSingleResult();
    }

    @Override
    public long insert(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment.getId();
        }
        return em.merge(comment).getId();
    }

    @Override
    public Comment getById(long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public List<Comment> getAll() {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }
}
