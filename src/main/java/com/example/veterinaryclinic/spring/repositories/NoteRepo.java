package com.example.veterinaryclinic.spring.repositories;

import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.FolderModel;
import com.example.veterinaryclinic.spring.models.NoteModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NoteRepo extends JpaRepository<NoteModel, Long> {
    List<NoteModel> findByFolderModelOrderByPatientModelAscUpdateDateDesc(FolderModel folderModel);
    List<NoteModel> findByFolderModelAndPatientModelOrderByPatientModelAscUpdateDateDesc(FolderModel folderModel, PatientModel patientModel);
}


