package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.entity.User;

@Service
public class UserServiceImpl implements UserService {

    private final IOService ioService;

    public UserServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public User getUser() {
        ioService.out("What's your name?");
        String name = ioService.readString();
        ioService.out("What's your surname?");
        String surname = ioService.readString();
        return new User(name, surname);
    }
}