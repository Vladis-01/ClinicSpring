package com.example.clinicspring.spring.repositories;

import com.example.clinicspring.spring.entities.Appointment;
import com.example.clinicspring.spring.entities.Doctor;
import com.example.clinicspring.spring.entities.Patient;
import com.example.clinicspring.spring.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppoimentRepo extends JpaRepository<Appointment, Long> {
    // Фильтр для того чтобы узнать есть ли запись на выбранное время у пациента или доктора
    Appointment findByDateAppointmentAndDoctorOrDateAppointmentAndPatient(Date dateAppoiment, Doctor doctor,
                                                                          Date dateAppoiment2, Patient patient);

    List<Appointment> findByPatientOrderByDateAppointment(Patient patient);

    List<Appointment> findByDateAppointmentBeforeAndStatus(Date dateAppointment, Status status);
}
