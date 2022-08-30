package com.example.veterinaryclinic.spring.mappings;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.DTO.NoteDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
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
}
