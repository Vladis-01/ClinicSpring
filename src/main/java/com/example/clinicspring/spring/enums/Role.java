package com.example.clinicspring.spring.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    DOCTOR,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
