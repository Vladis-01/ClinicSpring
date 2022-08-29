package com.example.veterinaryclinic.spring.Mappings;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.DTO.DoctorDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.entities.Appointment;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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
