package com.example.clinicspring.spring.services;

import com.example.clinicspring.spring.DTO.AppointmentDto;
import com.example.clinicspring.spring.DTO.DoctorDto;
import com.example.clinicspring.spring.DTO.PatientDto;
import com.example.clinicspring.spring.entities.Appointment;
import com.example.clinicspring.spring.enums.Status;
import com.example.clinicspring.spring.mappings.MappingAppointment;
import com.example.clinicspring.spring.mappings.MappingDoctors;
import com.example.clinicspring.spring.mappings.MappingPatients;
import com.example.clinicspring.spring.repositories.AppoimentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppoimentRepo appoimentRepo;
    private final MappingAppointment mappingAppointment;
    private final MappingDoctors mappingDoctors;
    private final MappingPatients mappingPatients;

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

    public List<AppointmentDto> getAppointmentsByDateAppointmentBeforeByStatus(Date dateAppoiment, Status status){
        return appoimentRepo.findByDateAppointmentBeforeAndStatus(dateAppoiment, status).stream()
                .map(mappingAppointment::mapToAppointmentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createOrUpdateAppointment(AppointmentDto appointment){
        appoimentRepo.save(mappingAppointment.mapToAppointmentEntity(appointment));
    }

    @Transactional
    public void createOrUpdateAppointments(List<AppointmentDto> appointmentListDTO){
        List appointmentList = new ArrayList<Appointment>();
        for (AppointmentDto appointmentDto: appointmentListDTO) {
            appointmentList.add(mappingAppointment.mapToAppointmentEntity(appointmentDto));
        }
        appoimentRepo.saveAll(appointmentList);
    }

    public void deleteAppointment(AppointmentDto appointment){
        appoimentRepo.delete(mappingAppointment.mapToAppointmentEntity(appointment));
    }

    public void deleteAppointmentById(Long appointmentDtoId){
        appoimentRepo.deleteById(appointmentDtoId);
    }
}
