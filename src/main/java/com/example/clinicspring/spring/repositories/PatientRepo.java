package com.example.clinicspring.spring.repositories;

import com.example.clinicspring.spring.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUsername(String username);

    Set<Patient> findByFullNameContainingIgnoreCase(String name);
}
