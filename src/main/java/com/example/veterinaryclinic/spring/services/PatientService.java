package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.enums.Role;
import com.example.veterinaryclinic.spring.mappings.MappingPatients;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatientService implements UserDetailsService {
    private final PatientRepo patientRepo;
    private final MappingPatients mappingPatients;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepo patientRepo, MappingPatients mappingPatients, PasswordEncoder passwordEncoder) {
        this.patientRepo = patientRepo;
        this.mappingPatients = mappingPatients;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return patientRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " not authorized."));
    }

    public PatientDto getPatientByUsername(String username) throws UsernameNotFoundException {
        return mappingPatients.mapToPatientDto(patientRepo.findByUsername(username).orElse(null));
    }

    public PatientDto getPatientById(Long id) throws UsernameNotFoundException {
        return mappingPatients.mapToPatientDto(patientRepo.findById(id).orElse(null));
    }

    public Set<PatientDto> getPatientsByFullName(String fullName){
        return patientRepo.findByFullNameContainingIgnoreCase(fullName).stream()
                .map(mappingPatients::mapToPatientDto)
                .collect(Collectors.toSet());
    }

    public Set<PatientDto> getAllPatients(){
        return patientRepo.findAll().stream()
                .map(mappingPatients::mapToPatientDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void createOrUpdatePatient(PatientDto patientDto){
        if (patientDto.getId() == null){
            patientDto.setPassword(passwordEncoder.encode(""));
            patientDto.setDateRegistration(new Date());
        }

        patientDto.setRole(Collections.singleton(Role.USER));
        patientRepo.save(mappingPatients.mapToPatientEntity(patientDto));
    }

    public void deletePatient(PatientDto patientDto){
        patientRepo.delete(mappingPatients.mapToPatientEntity(patientDto));
    }

    public void deletePatientById(Long patientDtoId){
        patientRepo.deleteById(patientDtoId);
    }
}
