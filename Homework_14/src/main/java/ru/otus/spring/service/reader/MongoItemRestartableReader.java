package ru.otus.spring.service.reader;

import org.springframework.batch.item.data.MongoItemReader;

public class MongoItemRestartableReader<T> extends MongoItemReader<T> {

    public void setPage(int page) {
        this.page = page;
    }
}
