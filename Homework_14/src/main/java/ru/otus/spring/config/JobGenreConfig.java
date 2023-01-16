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
import ru.otus.spring.domain.mongo.Genre;
import ru.otus.spring.service.reader.MongoItemRestartableReader;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobGenreConfig {

    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch-Genre");
    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;
    private final DataSource dataSource;

    @Bean
    public MongoItemRestartableReader<Genre> genreReader() {
        MongoItemRestartableReader<Genre> reader = new MongoItemRestartableReader<>();
        reader.setName("genreReader");
        reader.setTemplate(mongoTemplate);
        reader.setQuery("{}");
        reader.setTargetType(Genre.class);
        reader.setSort(new HashMap<>());
        return reader;
    }

    @Bean
    public ItemProcessor<Genre, Genre> genreProcessor() {
        return genre -> genre;
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreWriter() {
        return new JdbcBatchItemWriterBuilder<Genre>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("insert into genre (id, title) values (:id, :title)")
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public Step copyGenresStep(MongoItemRestartableReader<Genre> genreReader) {
        return stepBuilderFactory.get("copyGenresStep")
            .listener(new StepExecutionListener() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                    genreReader.setPage(0);
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    return stepExecution.getExitStatus();
                }
            })
            .<Genre, Genre>chunk(CHUNK_SIZE)
            .reader(genreReader)
            .processor(genreProcessor())
            .writer(genreWriter())
            .listener(new ItemReadListener<>() {
                public void beforeRead() {
                    logger.info("Start reading genre");
                }

                public void afterRead(@NonNull Genre o) {
                    logger.info("End reading genre {}", o.getId());
                }

                public void onReadError(@NonNull Exception e) {
                    logger.error("Error of reading genre: {}", e.getMessage());
                }
            })
            .listener(new ItemWriteListener<>() {
                public void beforeWrite(@NonNull List list) {
                    logger.info("Start recording genre");
                }

                public void afterWrite(@NonNull List list) {
                    logger.info("End recording genre");
                }

                public void onWriteError(@NonNull Exception e, @NonNull List list) {
                    logger.error("Error of recording genre: {}", e.getMessage());
                }
            })
            .listener(new ItemProcessListener<>() {
                public void beforeProcess(Genre o) {
                    logger.info("Start processing genre {}", o.getId());
                }

                public void afterProcess(@NonNull Genre o, Genre o2) {
                    logger.info("End processing genre {}", o.getId());
                }

                public void onProcessError(@NonNull Genre o, @NonNull Exception e) {
                    logger.error("Error of processing genre {}: {}", o.getId(), e.getMessage());
                }
            })
            .listener(new ChunkListener() {
                public void beforeChunk(@NonNull ChunkContext chunkContext) {
                    logger.info("Start chunking genre");
                }

                public void afterChunk(@NonNull ChunkContext chunkContext) {
                    logger.info("End chunking genre");
                }

                public void afterChunkError(@NonNull ChunkContext chunkContext) {
                    logger.error("Error of chunking genre");
                }
            })
            .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }
}
