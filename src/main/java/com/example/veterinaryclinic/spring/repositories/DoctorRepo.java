package com.example.veterinaryclinic.spring.repositories;

import com.example.veterinaryclinic.spring.enums.Position;
import com.example.veterinaryclinic.spring.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUsername(String username);
    List<Doctor> findByPosition(Position position);
}
