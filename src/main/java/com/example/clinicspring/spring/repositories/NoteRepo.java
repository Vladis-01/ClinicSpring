package com.example.clinicspring.spring.repositories;

import com.example.clinicspring.spring.entities.Folder;
import com.example.clinicspring.spring.entities.Note;
import com.example.clinicspring.spring.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findByFolderOrderByPatientAscUpdateDateDesc(Folder folder);
    List<Note> findByFolderAndPatientOrderByPatientAscUpdateDateDesc(Folder folder, Patient patient);

    Note findByNameAndFolder(String name, Folder folder);
}


