package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.FolderDto;
import com.example.veterinaryclinic.spring.DTO.NoteDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.Mappings.MappingFolder;
import com.example.veterinaryclinic.spring.entities.Folder;
import com.example.veterinaryclinic.spring.repositories.FolderRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FolderService {
    private final MappingFolder mappingFolder;
    private final FolderRepo folderRepo;

    public FolderService(MappingFolder mappingFolder, FolderRepo folderRepo) {
        this.mappingFolder = mappingFolder;
        this.folderRepo = folderRepo;
    }

    public void createOrUpdateFolder(FolderDto folder){
        folderRepo.save(mappingFolder.mapToFolderEntity(folder));
    }

    public void deleteFolder(FolderDto folder){
        folderRepo.delete(mappingFolder.mapToFolderEntity(folder));
    }

    public Set<FolderDto> getFoldersByParent(FolderDto parent){
        return folderRepo.findByParent(mappingFolder.mapToFolderEntity(parent)).stream()
                .map(mappingFolder::mapToFolderDto)
                .collect(Collectors.toSet());
    }

    public FolderDto getFolderByName(String name){
        return mappingFolder.mapToFolderDto(folderRepo.findByName(name));
    }

    public FolderDto getFolderByParentAndName(FolderDto parent, String name){
        return mappingFolder.mapToFolderDto(folderRepo.findByParentAndName(mappingFolder.mapToFolderEntity(parent), name));
    }

    public FolderDto getFolderById(Long id) {
        return mappingFolder.mapToFolderDto(folderRepo.findById(id).orElse(null));
    }

    public void deleteFolderById(Long folderDtoId){
        folderRepo.deleteById(folderDtoId);
    }
}
