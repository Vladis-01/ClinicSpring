package com.example.veterinaryclinic.spring.entities;

import com.example.veterinaryclinic.spring.enums.Status;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name="appointments")
public class Appointment {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAppointment;
    private Double price;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ElementCollection(targetClass = Status.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "appointment_status", joinColumns = @JoinColumn(name = "appointment_id"))
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "appointment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    private Set<Status> status;

}
