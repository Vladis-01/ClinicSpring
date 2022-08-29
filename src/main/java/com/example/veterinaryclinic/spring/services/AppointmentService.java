package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.DTO.DoctorDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.Mappings.MappingAppointment;
import com.example.veterinaryclinic.spring.Mappings.MappingDoctors;
import com.example.veterinaryclinic.spring.Mappings.MappingPatients;
import com.example.veterinaryclinic.spring.entities.Appointment;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppoimentRepo appoimentRepo;
    private final MappingAppointment mappingAppointment;
    private final MappingDoctors mappingDoctors;
    private final MappingPatients mappingPatients;

    public AppointmentService(AppoimentRepo appoimentRepo, MappingAppointment mappingAppointment, MappingDoctors mappingDoctors, MappingPatients mappingPatients) {
        this.appoimentRepo = appoimentRepo;
        this.mappingAppointment = mappingAppointment;
        this.mappingDoctors = mappingDoctors;
        this.mappingPatients = mappingPatients;
    }

    public List<AppointmentDto> getAllAppointments(){
        return appoimentRepo.findAll().stream()
                .map(mappingAppointment::mapToAppointmentDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentsByDateAppointmentAndDoctorOrDateAppointmentAndPatient(Date dateAppoiment, DoctorDto doctor,
                                                                                               Date dateAppoiment2, PatientDto patient){
        return mappingAppointment.mapToAppointmentDto(appoimentRepo.findByDateAppointmentAndDoctorOrDateAppointmentAndPatient(
                dateAppoiment, mappingDoctors.mapToDoctorEntity(doctor),
                dateAppoiment2, mappingPatients.mapToPatientEntity(patient)));
    }

    public List<AppointmentDto> getAppointmentByPatientOrderByDateAppointment(PatientDto patient){
        return appoimentRepo.findByPatientOrderByDateAppointment(mappingPatients.mapToPatientEntity(patient)).stream()
                .map(mappingAppointment::mapToAppointmentDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentById(Long id){
        return mappingAppointment.mapToAppointmentDto(appoimentRepo.findById(id).orElse(null));
    }

    public void createOrUpdateAppointment(AppointmentDto appointment){
        appoimentRepo.save(mappingAppointment.mapToAppointmentEntity(appointment));
    }

    public void deleteAppointment(AppointmentDto appointment){
        appoimentRepo.delete(mappingAppointment.mapToAppointmentEntity(appointment));
    }

    public void deleteAppointmentById(Long appointmentDtoId){
        appoimentRepo.deleteById(appointmentDtoId);
    }
}
