package ru.otus.spring.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.AppProps;
import ru.otus.spring.entity.User;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final IOService ioService;
    private final MessageSource messageSource;
    private final AppProps appProps;

    public UserServiceImpl(IOService ioService, MessageSource messageSource, AppProps appProps) {
        this.ioService = ioService;
        this.messageSource = messageSource;
        this.appProps = appProps;
    }

    @Override
    public User getUser() {
        String localeNameQuestion = messageSource.getMessage("user.name", null, appProps.getLocale());
        ioService.out(localeNameQuestion);
        String name = ioService.readString();
        String localeSurnameQuestion = messageSource.getMessage("user.surname", null, appProps.getLocale());
        ioService.out(localeSurnameQuestion);
        String surname = ioService.readString();
        return new User(name, surname);
    }
}