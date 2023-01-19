package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Junior;
import ru.otus.spring.domain.Student;

import static java.util.concurrent.ThreadLocalRandom.current;

@Service
public class StudentServiceImpl implements StudentService {
    @Override
    public Junior study(Student student) {
        System.out.printf(">>>>>>>>>>>>>> Student %s begin study\n", student);
        for (int i = 1; i < current().nextInt(1, 10); i++) {
            student.setAge(student.getAge() + 6);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Junior junior = new Junior(student.getId(),
                student.getProgrammerType(), student.getAge(), student.getExperience());
        System.out.printf(">>>>>>>>>>>>>> Junior %s is ready\n", junior);
        return junior;
    }
}
