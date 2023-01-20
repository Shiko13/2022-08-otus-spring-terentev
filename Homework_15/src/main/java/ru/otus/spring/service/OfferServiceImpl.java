package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.ProgrammerEntity;

@Service
public class OfferServiceImpl implements OfferService {

    @Override
    public void offerVacancy(ProgrammerEntity programmerEntity) {
        System.out.println(">>>>>>>>>>>>>> Google offers job: " + programmerEntity);
    }
}
