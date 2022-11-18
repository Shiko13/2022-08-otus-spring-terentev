package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ru.otus.spring.configuration.AppProps;
import ru.otus.spring.service.TestService;

@SpringBootApplication
@EnableConfigurationProperties(AppProps.class)
public class Homework3Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Homework3Application.class, args);
        TestService testService = context.getBean(TestService.class);
        testService.run();
    }
}
