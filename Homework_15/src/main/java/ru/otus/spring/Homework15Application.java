package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.spring.service.SeniorService;

@SpringBootApplication
public class Homework15Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Homework15Application.class);
        SeniorService seniorService = context.getBean(SeniorService.class);
        seniorService.climbTheLadder();
    }
}