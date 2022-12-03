package com.example.clinicspring.spring.services;

import com.example.clinicspring.spring.DTO.DoctorDto;
import com.example.clinicspring.spring.enums.Position;
import com.example.clinicspring.spring.enums.Role;
import com.example.clinicspring.spring.mappings.MappingDoctors;
import com.example.clinicspring.spring.entities.Doctor;
import com.example.clinicspring.spring.repositories.DoctorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService implements UserDetailsService {
    private final DoctorRepo doctorRepo;
    private final PasswordEncoder passwordEncoder;
    private final MappingDoctors mappingDoctors;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return doctorRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " not authorized."));
    }

    public Set<DoctorDto> getAllDoctors(){
        return doctorRepo.findAll().stream()
                .map(mappingDoctors::mapToDoctorDto)
                .collect(Collectors.toSet());
    }

    public DoctorDto getDoctorByUsername(String username){
        return mappingDoctors.mapToDoctorDto(doctorRepo.findByUsername(username).orElse(null));
    }

    public DoctorDto getDoctorById(Long id){
        return mappingDoctors.mapToDoctorDto(doctorRepo.findById(id).orElse(null));
    }

    public List<DoctorDto> getDoctorByPosition(Position position){
        return doctorRepo.findByPosition(position).stream()
                .map(mappingDoctors::mapToDoctorDto)
                .collect(Collectors.toList());
    }

    public void deleteDoctor(DoctorDto doctorDto){
        doctorRepo.delete(mappingDoctors.mapToDoctorEntity(doctorDto));
    }

    public void deleteDoctorById(Long doctorDtoId){
        doctorRepo.deleteById(doctorDtoId);
    }

    @Transactional
    public void createOrUpdateDoctor(DoctorDto doctor, Map<String, String> form){
        Set<String> positions = Arrays.stream(Position.values())
                .map(Position::name)
                .collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if (positions.contains(key)) {
                // Создаем Set в случае create
                if(doctor.getPosition() == null){
                    doctor.setPosition(new HashSet<>());
                }
                doctor.getPosition().add(Position.valueOf(key));
            }
        }

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                // Создаем Set в случае create
                if(doctor.getRole() == null){
                    doctor.setRole(new HashSet<>());
                }
                doctor.getRole().add(Role.valueOf(key));
            }
        }

        if (doctor.getId() == null){
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        } else {
            Doctor userFromDb = doctorRepo.findById(doctor.getId()).orElse(null);
            if (!passwordEncoder.matches(userFromDb.getPassword(), doctor.getPassword())){
                doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            }
        }

        doctorRepo.save(mappingDoctors.mapToDoctorEntity(doctor));
    }
}