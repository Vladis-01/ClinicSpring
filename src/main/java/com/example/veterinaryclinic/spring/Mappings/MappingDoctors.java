package com.example.veterinaryclinic.spring.Mappings;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.DTO.DoctorDto;
import com.example.veterinaryclinic.spring.DTO.FolderDto;
import com.example.veterinaryclinic.spring.DTO.NoteDto;
import com.example.veterinaryclinic.spring.entities.Appointment;
import com.example.veterinaryclinic.spring.entities.Doctor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MappingDoctors {
    private final ModelMapper modelMapper;

    public MappingDoctors(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //из entity в dto
    public DoctorDto mapToDoctorDto(Doctor doctor){
        if(doctor==null){
            return null;
        }

        DoctorDto doctorDto = modelMapper.map(doctor, DoctorDto.class);
        if(doctor.getAppointments() != null){
            doctorDto.setAppointmentsDto(modelMapper.map(doctor.getAppointments(), new TypeToken<Set<AppointmentDto>>() {}.getType()));
        }

        return doctorDto;
    }
    //из dto в entity
    public Doctor mapToDoctorEntity(DoctorDto dto){
        Doctor doctor = modelMapper.map(dto, Doctor.class);

        return doctor;
    }

//    //из entity в dto
//    public DoctorDto mapToDoctorDto(Doctor entity){
//        DoctorDto dto = new DoctorDto();
//        dto.setId(entity.getId());
//        dto.setUsername(entity.getUsername());
//        dto.setFullName(entity.getFullName());
//        dto.setPassword(entity.getPassword());
//        dto.setPosition(entity.getPosition());
//        dto.setRole(entity.getRole());
//        dto.setAppointmentsDto(entity.getAppointments());
//
//        return dto;
//    }
//    //из dto в entity
//    public Doctor mapToDoctorEntity(DoctorDto dto){
//        Doctor entity = new Doctor();
//        entity.setId(dto.getId());
//        entity.setUsername(dto.getUsername());
//        entity.setFullName(dto.getFullName());
//        entity.setPassword(dto.getPassword());
//        entity.setPosition(dto.getPosition());
//        entity.setRole(dto.getRole());
//        entity.setAppointments(dto.getAppointmentsDto());
//
//        return entity;
//    }
}
