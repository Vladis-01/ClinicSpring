package com.example.clinicspring.spring.DTO;

import com.example.clinicspring.spring.enums.Status;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class AppointmentDto {
    private Long id;

    private Date dateAppointment;

    private Double price;

    private String description;

    private DoctorDto doctorDto;

    private PatientDto patientDto;

    private List<MedicineDto> medicinesDto;

    private Set<Status> status;
}
