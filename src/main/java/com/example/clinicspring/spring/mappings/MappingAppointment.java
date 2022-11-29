package com.example.clinicspring.spring.mappings;

import com.example.clinicspring.spring.DTO.AppointmentDto;
import com.example.clinicspring.spring.DTO.DoctorDto;
import com.example.clinicspring.spring.DTO.PatientDto;
import com.example.clinicspring.spring.entities.Appointment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MappingAppointment {
    private final ModelMapper modelMapper;

    public MappingAppointment(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //из entity в dto
    public AppointmentDto mapToAppointmentDto(Appointment appointment){
        if(appointment==null){
            return null;
        }

        AppointmentDto appointmentDto = modelMapper.map(appointment, AppointmentDto.class);
        if(appointment.getDoctor() != null){
            appointmentDto.setDoctorDto(modelMapper.map(appointment.getDoctor(), DoctorDto.class));
        }
        if(appointment.getPatient() != null){
            appointmentDto.setPatientDto(modelMapper.map(appointment.getPatient(), PatientDto.class));
        }

        return appointmentDto;
    }
    //из dto в entity
    public Appointment mapToAppointmentEntity(AppointmentDto dto){
        Appointment appointment = modelMapper.map(dto, Appointment.class);

        return appointment;
    }
}
