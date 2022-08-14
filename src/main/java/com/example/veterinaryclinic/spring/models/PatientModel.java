package com.example.veterinaryclinic.spring.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="patients")
public class PatientModel implements UserDetails {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "username cannot be empty")
    private String username;
    @NotBlank(message = "fullName cannot be empty")
    private String fullName;

    @NotBlank(message = "password cannot be empty")
    @Size(min=5, message = "At least 5 characters")
    private String password;

    private Date dateRegistration;

    @OneToMany(mappedBy = "patientModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AppointmentModel> appointments;

    @OneToMany(mappedBy = "patientModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<NoteModel> notes;

    @Override
    public String toString(){
        return fullName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}