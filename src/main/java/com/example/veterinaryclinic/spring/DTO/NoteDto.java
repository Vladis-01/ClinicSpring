package com.example.veterinaryclinic.spring.DTO;

import com.example.veterinaryclinic.spring.entities.Folder;
import com.example.veterinaryclinic.spring.entities.Patient;
import lombok.Data;
import java.time.Instant;

@Data
public class NoteDto {
    private Long id;

    private String name;

    private String text;

    private Instant creationDate;

    private Instant updateDate;

    private FolderDto folderDto;

    private PatientDto patientDto;
}