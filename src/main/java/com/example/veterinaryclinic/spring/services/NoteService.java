package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.entities.Note;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.repositories.NoteRepo;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private final NoteRepo noteRepo;

    public NoteService(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public void createOrUpdateNote(Note note, Patient patient){
        note.setPatient(patient);

        noteRepo.save(note);
    }
}
