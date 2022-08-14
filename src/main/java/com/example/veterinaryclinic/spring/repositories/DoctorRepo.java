package com.example.veterinaryclinic.spring.repositories;

import com.example.veterinaryclinic.spring.Enums.Position;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface DoctorRepo extends JpaRepository<DoctorModel, Long> {
    DoctorModel findByUsername(String username);
    List<DoctorModel> findByPosition(Position position);
}
