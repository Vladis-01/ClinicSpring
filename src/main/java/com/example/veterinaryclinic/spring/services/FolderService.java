package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.FolderDto;
import com.example.veterinaryclinic.spring.mappings.MappingFolder;
import com.example.veterinaryclinic.spring.repositories.FolderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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
