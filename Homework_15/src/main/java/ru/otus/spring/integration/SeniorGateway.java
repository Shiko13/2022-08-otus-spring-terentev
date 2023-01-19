package ru.otus.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.domain.ProgrammerEntity;
import ru.otus.spring.domain.Student;

import java.util.List;

@MessagingGateway
public interface SeniorGateway {

    @Gateway(requestChannel = "studentsChannel", replyChannel = "seniorChannel")
    List<ProgrammerEntity> climbTheLadder(List<Student> students);
}
