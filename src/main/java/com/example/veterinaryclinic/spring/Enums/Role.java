package com.example.veterinaryclinic.spring.Enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    DOCTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
