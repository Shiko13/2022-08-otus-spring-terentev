package ru.otus.spring.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.domain.Middle;
import ru.otus.spring.domain.ProgrammerEntity;
import ru.otus.spring.domain.Senior;
import ru.otus.spring.service.JuniorService;
import ru.otus.spring.service.MiddleService;
import ru.otus.spring.service.OfferService;
import ru.otus.spring.service.StudentService;

import static java.util.concurrent.ThreadLocalRandom.current;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final StudentService studentService;
    private final JuniorService juniorService;
    private final MiddleService middleService;
    private final OfferService offerService;

    @Bean
    public QueueChannel studentsChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel juniorChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel middleChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel offerChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel unhappyProgrammersChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel happyProgrammersChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel readySeniorChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel seniorChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @ServiceActivator(inputChannel = "offerChannel")
    public void offerJob(ProgrammerEntity programmerEntity) {
        offerService.offerVacancy(programmerEntity);
    }

    @ServiceActivator(inputChannel = "happyProgrammersChannel")
    public void programmersBecomeHappy(ProgrammerEntity programmerEntity) {
        System.out.printf(">>>>>>>>>>>>>> Programmer becomes happy: %s\n", programmerEntity);
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(20).get();
    }

    @Bean
    public IntegrationFlow studentToJuniorFlow() {
        return IntegrationFlows.from(studentsChannel())
                .split()
                .handle(studentService, "study")
                .channel(juniorChannel())
                .get();
    }

    @Bean
    public IntegrationFlow juniorToMiddleFlow() {
        return IntegrationFlows.from(juniorChannel())
                .handle(juniorService, "job")
                .<Middle, Boolean>route(
                        c -> current().nextBoolean(),
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf.channel(middleChannel()))
                                .subFlowMapping(false, sf -> sf.channel(offerChannel())
                                        .channel(happyProgrammersChannel()))
                )
                .get();
    }

    @Bean
    public IntegrationFlow middleToSeniorFlow() {
        return IntegrationFlows.from(middleChannel())
                .handle(middleService, "hardJob")
                .<Senior>filter(b -> b.getExperience() > 0, e -> e.discardChannel(happyProgrammersChannel()))
                .channel(unhappyProgrammersChannel())
                .get();
    }

    @Bean
    public IntegrationFlow unhappyProgrammersFlow() {
        return IntegrationFlows.from(unhappyProgrammersChannel())
                .handle((GenericHandler<Senior>) (senior, headers) -> {
                    System.out.printf(">>>>>>>>>>>>>> Senior is unhappy %s\n", senior);
                    return senior;
                })
                .channel(readySeniorChannel())
                .get();
    }

    @Bean
    public IntegrationFlow happyProgrammersFlow() {
        return IntegrationFlows.from(happyProgrammersChannel())
                .handle((GenericHandler<ProgrammerEntity>) (senior, headers) -> {
                    senior.setHappy(true);
                    System.out.printf(">>>>>>>>>>>>>> Senior is happy %s\n", senior);
                    return senior;
                })
                .channel(readySeniorChannel())
                .get();
    }

    @Bean
    public IntegrationFlow seniorsFlow() {
        return IntegrationFlows.from(readySeniorChannel())
                .aggregate()
                .channel(seniorChannel())
                .get();
    }
}
