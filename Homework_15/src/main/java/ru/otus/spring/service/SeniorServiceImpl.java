package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.ProgrammerEntity;
import ru.otus.spring.domain.Student;
import ru.otus.spring.integration.SeniorGateway;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeniorServiceImpl implements SeniorService {

    private final SeniorGateway seniorGateway;
    private final StudentGenerator studentGenerator;
    @Override
    public void climbTheLadder() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 1; i <= 100; i++) {
            pool.execute(() -> {
                List<Student> students = studentGenerator.generate();
                System.out.printf(">>>>>>>>>>>>>> New students: %s\n", students.stream()
                        .map(Student::toString)
                        .collect(Collectors.toList()));
                List<ProgrammerEntity> seniors = seniorGateway.climbTheLadder(students);
                System.out.printf(">>>>>>>>>>>>>> Received seniors: %s\n", seniors.stream()
                        .map(ProgrammerEntity::toString)
                        .collect(Collectors.toList()));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
