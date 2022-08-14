package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.Enums.Position;
import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorService implements UserDetailsService {
    @Autowired
    private DoctorRepo doctorRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DoctorModel doctorModel = doctorRepo.findByUsername(username);
        if(doctorModel == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
        return doctorRepo.findByUsername(username);
    }

    public void createOrUpdateDoctor(DoctorModel doctorModel, Map<String, String> form){
        Set<String> positions = Arrays.stream(Position.values())
                .map(Position::name)
                .collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if (positions.contains(key)) {
                if(doctorModel.getPosition() == null){
                    doctorModel.setPosition(new HashSet<>());
                }
                doctorModel.getPosition().add(Position.valueOf(key));
            }
        }

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                if(doctorModel.getRole() == null){
                    doctorModel.setRole(new HashSet<>());
                }
                doctorModel.getRole().add(Role.valueOf(key));
            }
        }

        doctorRepo.save(doctorModel);
    }
}