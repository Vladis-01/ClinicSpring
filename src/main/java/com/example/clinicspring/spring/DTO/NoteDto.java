package com.example.clinicspring.spring.DTO;

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