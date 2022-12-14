package com.example.clinicspring.spring.services;

import com.example.clinicspring.spring.DTO.FolderDto;
import com.example.clinicspring.spring.DTO.NoteDto;
import com.example.clinicspring.spring.DTO.PatientDto;
import com.example.clinicspring.spring.mappings.MappingFolder;
import com.example.clinicspring.spring.mappings.MappingNotes;
import com.example.clinicspring.spring.mappings.MappingPatients;
import com.example.clinicspring.spring.repositories.NoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepo noteRepo;
    private final MappingNotes mappingNotes;
    private final MappingPatients mappingPatients;
    private final MappingFolder mappingFolder;

    @Transactional
    public void createOrUpdateNote(NoteDto note){
        noteRepo.save(mappingNotes.mapToNoteEntity(note));
    }

    public void deleteNote(NoteDto note){
        noteRepo.delete(mappingNotes.mapToNoteEntity(note));
    }

    public List<NoteDto> getNotesByFolderOrderByPatientAscUpdateDateDesc(FolderDto folder){
        return noteRepo.findByFolderOrderByPatientAscUpdateDateDesc(mappingFolder.mapToFolderEntity(folder)).stream()
                .map(mappingNotes::mapToNoteDto)
                .collect(Collectors.toList());
    }

    public List<NoteDto> getNotesByFolderAndPatientOrderByPatientAscUpdateDateDesc(FolderDto folder, PatientDto patient){
        return noteRepo.findByFolderAndPatientOrderByPatientAscUpdateDateDesc(mappingFolder.mapToFolderEntity(folder),
                        mappingPatients.mapToPatientEntity(patient)).stream()
                .map(mappingNotes::mapToNoteDto)
                .collect(Collectors.toList());
    }

    public NoteDto getNoteByNameAndFolder(String name, FolderDto folder){
        return mappingNotes.mapToNoteDto(noteRepo.findByNameAndFolder(name, mappingFolder.mapToFolderEntity(folder)));
    }

    public NoteDto getNoteById(Long id){
        return mappingNotes.mapToNoteDto(noteRepo.findById(id).orElse(null));
    }

    public List<NoteDto> getAllNotes() {
        return noteRepo.findAll().stream()
                .map(mappingNotes::mapToNoteDto)
                .collect(Collectors.toList());
    }

    public void deleteNoteById(Long noteDtoId){
        noteRepo.deleteById(noteDtoId);
    }
}
