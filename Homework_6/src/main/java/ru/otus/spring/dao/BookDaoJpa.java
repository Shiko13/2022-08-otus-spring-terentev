package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoJpa implements BookDao {

    private final EntityManager em;

    @Override
    public long count() {
        TypedQuery<Long> query = em.createQuery("select count(b.id) from Book b", Long.class);
        return query.getSingleResult();
    }

    @Override
    public Book insert(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Optional<Book> getById(long id) {
        TypedQuery<Book> query = em.createQuery("select b " +
                        "from Book b " +
                        "join fetch b.author " +
                        "join fetch b.genre " +
                        "where b.id = :id",
                Book.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select distinct b from Book b " +
                "join fetch b.author " +
                "join fetch b.genre", Book.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
