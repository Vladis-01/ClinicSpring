package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.models.NoteModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NoteService {
    @Autowired
    private NoteRepo noteRepo;

    public void createOrUpdateNote(NoteModel noteModel, PatientModel patientModel){
        noteModel.setCreationDate(Instant.now());
        noteModel.setUpdateDate(Instant.now());
        noteModel.setPatientModel(patientModel);

        noteRepo.save(noteModel);
    }
}
