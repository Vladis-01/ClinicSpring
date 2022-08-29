package com.example.veterinaryclinic.spring.DTO;

import com.example.veterinaryclinic.spring.entities.Note;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Id;
import java.util.List;
import java.util.Set;

@Setter
@Getter
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