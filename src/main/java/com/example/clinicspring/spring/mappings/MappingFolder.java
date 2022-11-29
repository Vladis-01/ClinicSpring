package com.example.clinicspring.spring.mappings;

import com.example.clinicspring.spring.DTO.FolderDto;
import com.example.clinicspring.spring.DTO.NoteDto;
import com.example.clinicspring.spring.entities.Folder;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
}
