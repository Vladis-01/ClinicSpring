package com.example.clinicspring.spring.DTO;

import com.example.clinicspring.spring.enums.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
public class PatientDto {
    private Long id;

    @NotBlank(message = "{NotBlank.Username}")
    @Size(min=2, message = "{Size.Username}")
    @Email
    private String username;

    @NotBlank(message = "{NotBlank.FullName}")
    @Size(min=5, message = "{Size.FullName}")
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
