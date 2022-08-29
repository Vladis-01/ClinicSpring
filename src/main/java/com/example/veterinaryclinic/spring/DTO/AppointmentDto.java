package com.example.veterinaryclinic.spring.DTO;

import com.example.veterinaryclinic.spring.Enums.Status;
import com.example.veterinaryclinic.spring.entities.Doctor;
import com.example.veterinaryclinic.spring.entities.Patient;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AppointmentDto {
    private Long id;

    private Date dateAppointment;

    private Double price;

    private String description;

    private DoctorDto doctorDto;

    private PatientDto patientDto;

    private Set<Status> status;
}
