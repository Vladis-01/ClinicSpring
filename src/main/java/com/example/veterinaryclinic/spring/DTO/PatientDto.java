package com.example.veterinaryclinic.spring.DTO;

import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.entities.Appointment;
import com.example.veterinaryclinic.spring.entities.Note;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class PatientDto {
    private Long id;

    private String username;

    private String fullName;

    private String password;

    private Date dateRegistration;

    private Set<AppointmentDto> appointmentsDto;

    private Set<NoteDto> notesDto;

    private Set<Role> role;

    @Override
    public String toString(){
        return fullName;
    }
}