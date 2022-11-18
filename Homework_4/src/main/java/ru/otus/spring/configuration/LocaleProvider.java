package ru.otus.spring.configuration;

import java.util.Locale;

public interface LocaleProvider {
    Locale getLocale();
    void setLocale(Locale locale);
}
