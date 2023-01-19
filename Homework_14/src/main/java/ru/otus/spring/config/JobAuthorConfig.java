package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import ru.otus.spring.domain.mongo.Author;
import ru.otus.spring.service.reader.MongoItemRestartableReader;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobAuthorConfig {

    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch-Author");
    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;
    private final DataSource dataSource;

    @Bean
    public MongoItemRestartableReader<Author> authorReader() {
        MongoItemRestartableReader<Author> reader = new MongoItemRestartableReader<>();
        reader.setName("authorReader");
        reader.setTemplate(mongoTemplate);
        reader.setQuery("{}");
        reader.setTargetType(Author.class);
        reader.setSort(new HashMap<>());
        return reader;
    }

    @Bean
    public ItemProcessor<Author, Author> authorProcessor() {
        return author -> author;
    }

    @Bean
    public JdbcBatchItemWriter<Author> authorWriter() {
        return new JdbcBatchItemWriterBuilder<Author>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into author (id, name) values (:id, :name)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step copyAuthorsStep(MongoItemRestartableReader<Author> authorReader) {
        return stepBuilderFactory.get("copyAuthorsStep")
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        authorReader.setPage(0);
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return stepExecution.getExitStatus();
                    }
                })
                .<Author, Author>chunk(CHUNK_SIZE)
                .reader(authorReader)
                .processor(authorProcessor())
                .writer(authorWriter())
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Start reading author");
                    }

                    public void afterRead(@NonNull Author o) {
                        logger.info("End reading author {}", o.getId());
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.error("Error of reading author: {}", e.getMessage());
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        logger.info("Start recording author");
                    }

                    public void afterWrite(@NonNull List list) {
                        logger.info("End recording author");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        logger.error("Error of recording author: {}", e.getMessage());
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(Author o) {
                        logger.info("Start processing author {}", o.getId());
                    }

                    public void afterProcess(@NonNull Author o, Author o2) {
                        logger.info("End processing author {}", o.getId());
                    }

                    public void onProcessError(@NonNull Author o, @NonNull Exception e) {
                        logger.error("Error of processing author {}: {}", o.getId(), e.getMessage());
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Start chunking author");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("End chunking author");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.error("Error of chunking author");
                    }
                })
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}
