package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import ru.otus.spring.domain.jpa.AuthorJpa;
import ru.otus.spring.domain.jpa.BookJpa;
import ru.otus.spring.domain.jpa.CommentJpa;
import ru.otus.spring.domain.jpa.GenreJpa;
import ru.otus.spring.domain.mongo.Book;
import ru.otus.spring.service.reader.MongoItemRestartableReader;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class JobBookConfig {

    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch-Book");

    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public MongoItemRestartableReader<Book> bookReader() {
        MongoItemRestartableReader<Book> reader = new MongoItemRestartableReader<>();
        reader.setName("bookReader");
        reader.setTemplate(mongoTemplate);
        reader.setQuery("{}");
        reader.setTargetType(Book.class);
        reader.setSort(new HashMap<>());
        return reader;
    }

    @Bean
    public ItemProcessor<Book, BookJpa> bookProcessor() {
        return book -> new BookJpa(
            book.getId(),
            book.getTitle(),
            new AuthorJpa(book.getAuthor().getId()),
            book.getGenres().stream().map(g -> new GenreJpa(g.getId())).collect(Collectors.toList()),
            book.getComments().stream()
                    .map(c -> new CommentJpa(c.getId(), c.getText())).collect(Collectors.toList())
        );
    }

    @Bean
    public JpaItemWriter<BookJpa> bookWriter() {
        return new JpaItemWriterBuilder<BookJpa>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }

    @Bean
    public Step copyBooksStep(MongoItemRestartableReader<Book> bookReader) {
        return stepBuilderFactory.get("copyBooksStep")
            .listener(new StepExecutionListener() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                    bookReader.setPage(0);
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    return stepExecution.getExitStatus();
                }
            })
            .<Book, BookJpa>chunk(CHUNK_SIZE)
            .reader(bookReader)
            .processor(bookProcessor())
            .writer(bookWriter())
            .listener(new ItemReadListener<>() {
                public void beforeRead() {
                    logger.info("Start reading book");
                }

                public void afterRead(@NonNull Book o) {
                    logger.info("End reading book {}", o.getId());
                }

                public void onReadError(@NonNull Exception e) {
                    logger.error("Error of reading book: {}", e.getMessage());
                }
            })
            .listener(new ItemWriteListener<>() {
                public void beforeWrite(@NonNull List list) {
                    logger.info("Start recording book");
                }

                public void afterWrite(@NonNull List list) {
                    logger.info("End recording book");
                }

                public void onWriteError(@NonNull Exception e, @NonNull List list) {
                    logger.error("Error of recording book: {}", e.getMessage());
                }
            })
            .listener(new ItemProcessListener<>() {
                public void beforeProcess(Book o) {
                    logger.info("Start processing book {}", o.getId());
                }

                public void afterProcess(@NonNull Book o, BookJpa o2) {
                    logger.info("End processing book {}", o.getId());
                }

                public void onProcessError(@NonNull Book o, @NonNull Exception e) {
                    logger.error("Error of processing book {}: {}", o.getId(), e.getMessage());
                }
            })
            .listener(new ChunkListener() {
                public void beforeChunk(@NonNull ChunkContext chunkContext) {
                    logger.info("Start chunking book");
                }

                public void afterChunk(@NonNull ChunkContext chunkContext) {
                    logger.info("End chunking book");
                }

                public void afterChunkError(@NonNull ChunkContext chunkContext) {
                    logger.error("Error of chunking book");
                }
            })
            .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }
}
