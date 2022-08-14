package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PatientService implements UserDetailsService {
    @Autowired
    private PatientRepo patientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PatientModel patientModel = patientRepo.findByUsername(username);
        if(patientModel == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
        return patientRepo.findByUsername(username);
    }
}
