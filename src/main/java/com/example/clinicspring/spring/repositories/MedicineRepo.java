package com.example.clinicspring.spring.repositories;

import com.example.clinicspring.spring.entities.Appointment;
import com.example.clinicspring.spring.entities.Folder;
import com.example.clinicspring.spring.entities.Medicine;
import com.example.clinicspring.spring.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepo extends JpaRepository<Medicine, Long> {
    Medicine findByPackingIdAndAppointment(Long packingId, Appointment appointment);
}
