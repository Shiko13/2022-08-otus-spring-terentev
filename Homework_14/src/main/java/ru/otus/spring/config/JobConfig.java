package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import ru.otus.spring.service.ExecuteSqlScriptService;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final String COPY_LIBRARY_JOB_NAME = "copyLibraryJob";
    public static final String INIT_SCHEMA_FILE_NAME = "initSchemaFileName";
    public static final String UPDATE_SCHEMA_FILE_NAME = "updateSchemaFileName";
    private final Logger logger = LoggerFactory.getLogger("Batch");
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ExecuteSqlScriptService executeSqlScriptService;

    @Bean
    @StepScope
    public Tasklet schemaInitTasklet(@Value("#{jobParameters['" +
            INIT_SCHEMA_FILE_NAME + "']}") String initSchemaFileName) {
        return (contribution, chunkContext) -> {
                executeSqlScriptService.executeSqlScript(initSchemaFileName);
                return RepeatStatus.FINISHED;
            };
    }

    @Bean
    public Step schemaInitStep(Tasklet schemaInitTasklet) {
        return stepBuilderFactory.get("schemaInitStep")
            .allowStartIfComplete(true)
            .tasklet(schemaInitTasklet)
            .build();
    }

    @StepScope
    @Bean
    public Tasklet schemaUpdateTasklet(@Value("#{jobParameters['" +
            UPDATE_SCHEMA_FILE_NAME + "']}") String updateSchemaFileName) {
        return (contribution, chunkContext) -> {
                executeSqlScriptService.executeSqlScript(updateSchemaFileName);
                return RepeatStatus.FINISHED;
            };
    }

    @Bean
    public Step schemaUpdateStep(Tasklet schemaUpdateTasklet) {
        return stepBuilderFactory.get("schemaUpdateStep")
            .allowStartIfComplete(true)
            .tasklet(schemaUpdateTasklet)
            .build();
    }

    @Bean
    public Job copyLibraryJob(Step schemaInitStep,
                              Step schemaUpdateStep,
                              Step copyAuthorsStep,
                              Step copyGenresStep,
                              Step copyBooksStep) {
        return jobBuilderFactory.get(COPY_LIBRARY_JOB_NAME)
            .incrementer(new RunIdIncrementer())
            .start(schemaInitStep)
            .next(copyAuthorsStep)
            .next(copyGenresStep)
            .next(copyBooksStep)
            .next(schemaUpdateStep)
            .listener(new JobExecutionListener() {
                @Override
                public void beforeJob(@NonNull JobExecution jobExecution) {
                    logger.info("Start job");
                }

                @Override
                public void afterJob(@NonNull JobExecution jobExecution) {
                    logger.info("End job");
                }
            })
            .build();
    }
}
