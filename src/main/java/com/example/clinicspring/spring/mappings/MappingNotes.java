package com.example.clinicspring.spring.mappings;

import com.example.clinicspring.spring.DTO.FolderDto;
import com.example.clinicspring.spring.DTO.NoteDto;
import com.example.clinicspring.spring.DTO.PatientDto;
import com.example.clinicspring.spring.entities.Note;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MappingNotes {
    private final ModelMapper modelMapper;

    public MappingNotes(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //из entity в dto
    public NoteDto mapToNoteDto(Note note) {
        if(note==null){
            return null;
        }

        NoteDto noteDto = modelMapper.map(note, NoteDto.class);
        if(note.getPatient() != null){
            noteDto.setPatientDto(modelMapper.map(note.getPatient(), PatientDto.class));
        }
        if(note.getFolder() != null){
            noteDto.setFolderDto(modelMapper.map(note.getFolder(), FolderDto.class));
        }

        return noteDto;
    }

    //из dto в entity
    public Note mapToNoteEntity(NoteDto dto) {
        Note doctor = modelMapper.map(dto, Note.class);

        return doctor;
    }
}