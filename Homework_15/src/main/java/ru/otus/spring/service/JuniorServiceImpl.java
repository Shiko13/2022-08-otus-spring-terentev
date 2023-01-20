package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Junior;
import ru.otus.spring.domain.Middle;

import static java.util.concurrent.ThreadLocalRandom.current;

@Service
public class JuniorServiceImpl implements JuniorService {
    @Override
    public Middle job(Junior junior) {
        System.out.printf(">>>>>>>>>>>>>> Junior %s begin job\n", junior);
        for (int i = 1; i < current().nextInt(1, 10); i++) {
            junior.setAge(junior.getAge() + 2);
            junior.setExperience(junior.getExperience() + 2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Middle middle = new Middle(junior.getId(),
                junior.getProgrammerType(), junior.getAge(), junior.getExperience());
        System.out.printf(">>>>>>>>>>>>>> Middle %s is ready\n", middle);
        return middle;
    }
}