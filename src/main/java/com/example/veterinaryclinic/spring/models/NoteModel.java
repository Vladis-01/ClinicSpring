package com.example.veterinaryclinic.spring.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name="files")
public class NoteModel {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String text;

    private Instant creationDate;
    private Instant updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "folder_id")
    private FolderModel folderModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private PatientModel patientModel;
}