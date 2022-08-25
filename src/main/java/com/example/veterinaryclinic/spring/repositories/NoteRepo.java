package com.example.veterinaryclinic.spring.repositories;

import com.example.veterinaryclinic.spring.entities.Folder;
import com.example.veterinaryclinic.spring.entities.Note;
import com.example.veterinaryclinic.spring.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findByFolderOrderByPatientAscUpdateDateDesc(Folder folder);
    List<Note> findByFolderAndPatientOrderByPatientAscUpdateDateDesc(Folder folder, Patient patient);

    Note findByNameAndFolder(String name, Folder folder);
}


