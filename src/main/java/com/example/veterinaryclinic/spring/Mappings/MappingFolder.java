package com.example.veterinaryclinic.spring.Mappings;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.DTO.FolderDto;
import com.example.veterinaryclinic.spring.DTO.NoteDto;
import com.example.veterinaryclinic.spring.entities.Appointment;
import com.example.veterinaryclinic.spring.entities.Folder;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MappingFolder {
    private final ModelMapper modelMapper;

    public MappingFolder(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //из entity в dto
    public FolderDto mapToFolderDto(Folder folder){
        if(folder==null){
            return null;
        }

        FolderDto folderDto = modelMapper.map(folder, FolderDto.class);
        if(folder.getParent() != null){
            folderDto.setParentFolderDto(modelMapper.map(folder.getParent(), FolderDto.class));
        }
        if(folder.getFolders() != null){
            folderDto.setFoldersDto(modelMapper.map(folder.getFolders(), new TypeToken<Set<FolderDto>>() {}.getType()));
        }
        if(folder.getNotes() != null){
            folderDto.setNotesDto(modelMapper.map(folder.getNotes(), new TypeToken<List<NoteDto>>() {}.getType()));
        }

        return folderDto;
    }
    //из dto в entity
    public Folder mapToFolderEntity(FolderDto dto){
        if(dto==null){
            return null;
        }
        Folder folder = modelMapper.map(dto, Folder.class);

        return folder;
    }
//    //из entity в dto
//    public FolderDto mapToFolderDto(Folder entity){
//        FolderDto dto = new FolderDto();
//        dto.setId(entity.getId());
//        dto.setName(entity.getName());
//        dto.setPath(entity.getPath());
//        dto.setParentId(entity.getParent().getId());
//        dto.setNotes(entity.getNotes());
//        dto.setFolders(entity.g());
//        dto.setAppointments(entity.getAppointments());
//
//        return dto;
//    }
//    //из dto в entity
//    public Folder mapToFolderEntity(FolderDto dto){
//        Folder entity = new Folder();
//        entity.setId(dto.getId());
//        entity.setUsername(dto.getUsername());
//        entity.setFullName(dto.getFullName());
//        entity.setPassword(dto.getPassword());
//        entity.setPosition(dto.getPosition());
//        entity.setRole(dto.getRole());
//        entity.setAppointments(dto.getAppointments());
//
//        return entity;
//    }
}
