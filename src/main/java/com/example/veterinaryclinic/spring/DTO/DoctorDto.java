package com.example.veterinaryclinic.spring.DTO;

import com.example.veterinaryclinic.spring.Enums.Position;
import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private Long id;

    private String username;

    private String password;

    private String fullName;

    private Set<AppointmentDto> appointmentsDto;

    private Set<Role> role;

    private Set<Position> position;

    @Override
    public String toString(){
        return fullName;
    }
}
