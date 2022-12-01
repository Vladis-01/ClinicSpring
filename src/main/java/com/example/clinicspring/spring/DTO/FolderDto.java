package com.example.clinicspring.spring.DTO;

import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FolderDto {
    private Long id;

    private String name;

    private FolderDto parentFolderDto;

    private Set<FolderDto> foldersDto;

    private List<NoteDto> notesDto;

    private String path;
}