package com.example.clinicspring.spring.services;

import com.example.clinicspring.spring.DTO.FolderDto;
import com.example.clinicspring.spring.mappings.MappingFolder;
import com.example.clinicspring.spring.repositories.FolderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final MappingFolder mappingFolder;
    private final FolderRepo folderRepo;

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
