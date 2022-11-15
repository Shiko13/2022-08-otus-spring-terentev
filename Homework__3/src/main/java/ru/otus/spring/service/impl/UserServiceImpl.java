package ru.otus.spring.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.LocaleProvider;
import ru.otus.spring.entity.User;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final IOService ioService;
    private final MessageSource messageSource;
    private final LocaleProvider localeProvider;

    public UserServiceImpl(IOService ioService, MessageSource messageSource, LocaleProvider localeProvider) {
        this.ioService = ioService;
        this.messageSource = messageSource;
        this.localeProvider = localeProvider;
    }

    @Override
    public User getUser() {
        String localeNameQuestion = messageSource.getMessage("user.name", null, localeProvider.getLocale());
        ioService.out(localeNameQuestion);
        String name = ioService.readString();
        String localeSurnameQuestion = messageSource.getMessage("user.surname", null, localeProvider.getLocale());
        ioService.out(localeSurnameQuestion);
        String surname = ioService.readString();
        return new User(name, surname);
    }
}