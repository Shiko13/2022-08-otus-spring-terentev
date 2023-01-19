package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Middle;
import ru.otus.spring.domain.Senior;

import static java.util.concurrent.ThreadLocalRandom.current;

@Service
public class MiddleServiceImpl implements MiddleService {
    @Override
    public Senior hardJob(Middle middle) {
        System.out.printf(">>>>>>>>>>>>>> Middle %s begin hard job\n", middle);
        for (int i = 1; i < current().nextInt(1, 20); i++) {
            middle.setAge(middle.getAge() + 4);
            middle.setExperience(middle.getExperience() + 4);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Senior senior = new Senior(middle.getId(),
                middle.getProgrammerType(), middle.getAge(), middle.getExperience());
        System.out.printf(">>>>>>>>>>>>>> Senior %s is ready\n", senior);
        return senior;
    }
}
