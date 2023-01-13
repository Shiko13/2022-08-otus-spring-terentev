package ru.otus.spring.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER, ANONYMOUS;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
