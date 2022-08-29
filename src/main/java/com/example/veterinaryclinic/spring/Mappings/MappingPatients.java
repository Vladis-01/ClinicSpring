package com.example.veterinaryclinic.spring.Mappings;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.DTO.FolderDto;
import com.example.veterinaryclinic.spring.DTO.NoteDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.entities.Folder;
import com.example.veterinaryclinic.spring.entities.Patient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MappingPatients {
    private final ModelMapper modelMapper;

    public MappingPatients(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //из entity в dto
    public PatientDto mapToPatientDto(Patient patient){
        if(patient==null){
            return null;
        }

        PatientDto patientDto = modelMapper.map(patient, PatientDto.class);
        if(patient.getAppointments() != null){
            patientDto.setAppointmentsDto(modelMapper.map(patient.getAppointments(), new TypeToken<Set<AppointmentDto>>() {}.getType()));
        }
        if(patient.getAppointments() != null){
            patientDto.setNotesDto(modelMapper.map(patient.getNotes(), new TypeToken<Set<NoteDto>>() {}.getType()));
        }

        return patientDto;
    }
    //из dto в entity
    public Patient mapToPatientEntity(PatientDto dto){
        Patient patient = modelMapper.map(dto, Patient.class);

        return patient;
    }
//    //из entity в dto
//    public PatientDto mapToProductDto(Patient entity){
//        PatientDto dto = new PatientDto();
//        dto.setId(entity.getId());
//        dto.setUsername(entity.getUsername());
//        dto.setFullName(entity.getFullName());
//        dto.setPassword(entity.getPassword());
//        dto.setDateRegistration(entity.getDateRegistration());
//        dto.setRole(entity.getRole());
//        dto.setAppointments(entity.getAppointments());
//        dto.setNotes(entity.getNotes());
//
//        return dto;
//    }
//    //из dto в entity
//    public Patient mapToPatientEntity(PatientDto dto){
//        Patient entity = new Patient();
//        entity.setId(dto.getId());
//        entity.setUsername(dto.getUsername());
//        entity.setFullName(dto.getFullName());
//        entity.setPassword(dto.getPassword());
//        entity.setDateRegistration(dto.getDateRegistration());
//        entity.setRole(dto.getRole());
//        entity.setAppointments(dto.getAppointments());
//        entity.setNotes(dto.getNotes());
//
//        return entity;
//    }
}
