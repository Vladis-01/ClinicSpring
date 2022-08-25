package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PatientService implements UserDetailsService {
    private final PatientRepo patientRepo;

    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return patientRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " not authorized."));
    }

}
