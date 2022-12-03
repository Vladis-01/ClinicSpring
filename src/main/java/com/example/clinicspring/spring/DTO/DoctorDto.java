package com.example.clinicspring.spring.DTO;

import com.example.clinicspring.spring.enums.Position;
import com.example.clinicspring.spring.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private Long id;

    @NotBlank(message = "{NotBlank.Username}")
    @Size(min=2, message = "{Size.Username}")
    private String username;

    private String password;

    @NotBlank(message = "{NotBlank.FullName}")
    @Size(min=5, message = "{Size.FullName}")
    private String fullName;

    private Set<AppointmentDto> appointmentsDto;

    private Set<Role> role;

    private Set<Position> position;

    @Override
    public String toString(){
        return fullName;
    }
}
