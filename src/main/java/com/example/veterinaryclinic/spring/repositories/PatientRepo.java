package com.example.veterinaryclinic.spring.repositories;

import com.example.veterinaryclinic.spring.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Set;

public interface PatientRepo extends JpaRepository<PatientModel, Long> {
    PatientModel findByUsername(String username);

    Set<PatientModel> findByFullNameContainingIgnoreCase(String name);
}
