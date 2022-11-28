package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.FolderDto;
import com.example.veterinaryclinic.spring.DTO.NoteDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.mappings.MappingFolder;
import com.example.veterinaryclinic.spring.mappings.MappingNotes;
import com.example.veterinaryclinic.spring.mappings.MappingPatients;
import com.example.veterinaryclinic.spring.repositories.NoteRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepo noteRepo;
    private final MappingNotes mappingNotes;
    private final MappingPatients mappingPatients;
    private final MappingFolder mappingFolder;

    public NoteService(NoteRepo noteRepo, MappingNotes mappingNotes, MappingPatients mappingPatients, MappingFolder mappingFolder) {
        this.noteRepo = noteRepo;
        this.mappingNotes = mappingNotes;
        this.mappingPatients = mappingPatients;
        this.mappingFolder = mappingFolder;
    }

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
