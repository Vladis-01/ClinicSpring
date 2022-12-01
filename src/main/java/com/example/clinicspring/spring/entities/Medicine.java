package com.example.clinicspring.spring.entities;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="medicines")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long packingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
