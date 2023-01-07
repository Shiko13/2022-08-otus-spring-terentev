package ru.otus.spring.repository;

import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;

public interface BookDaoCustom {
    Mono<Void> deleteCommentByIdAndBookId(String commentId, String bookId);

    Mono<Book> updateBookWithoutComments(Book newBook);
}
