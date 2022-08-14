package com.example.veterinaryclinic.spring.repositories;

import com.example.veterinaryclinic.spring.models.AppointmentModel;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.FolderModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Set;

public interface FolderRepo extends JpaRepository<FolderModel, Long> {
    FolderModel findByParentAndName(FolderModel folderModel, String name);
    Set<FolderModel> findByParent(FolderModel folderModel);
    FolderModel findByName(String name);
}
