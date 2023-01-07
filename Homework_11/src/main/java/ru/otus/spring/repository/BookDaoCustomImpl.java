package ru.otus.spring.repository;

import com.mongodb.DBRef;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;

@RequiredArgsConstructor
public class BookDaoCustomImpl implements BookDaoCustom {

    private final ReactiveMongoTemplate mongoTemplate;
    @Override
    public Mono<Void> deleteCommentByIdAndBookId(String commentId, String bookId) {
        Query query = new Query(Criteria.where("id").is(bookId));
        Update update = new Update().pull("comments", Query.query(Criteria.where("$id")
                .is(new ObjectId(commentId))));
        return mongoTemplate.updateMulti(query, update, Book.class).then();
    }

    @Override
    public Mono<Book> updateBookWithoutComments(Book newBook) {
        Query query = new Query(Criteria.where("id").is(newBook.getId()));
        Update update = new Update()
                .set("title", newBook.getTitle())
                .set("author", new DBRef("author", new ObjectId(newBook.getAuthor().getId())))
                .set("genre", new DBRef("genre", new ObjectId(newBook.getGenre().getId())));
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Book.class);
    }
}
