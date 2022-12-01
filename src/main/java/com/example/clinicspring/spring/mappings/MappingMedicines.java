package com.example.clinicspring.spring.mappings;

import com.example.clinicspring.spring.DTO.FolderDto;
import com.example.clinicspring.spring.DTO.MedicineDto;
import com.example.clinicspring.spring.DTO.NoteDto;
import com.example.clinicspring.spring.entities.Folder;
import com.example.clinicspring.spring.entities.Medicine;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MappingMedicines {
    private final ModelMapper modelMapper;

    public MappingMedicines(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //из entity в dto
    public MedicineDto mapToMedicineDto(Medicine medicine){
        if(medicine==null){
            return null;
        }

        MedicineDto medicineDto = modelMapper.map(medicine, MedicineDto.class);

        return medicineDto;
    }
    //из dto в entity
    public Medicine mapToMedicineEntity(MedicineDto dto){
        if(dto==null){
            return null;
        }
        Medicine medicine = modelMapper.map(dto, Medicine.class);

        return medicine;
    }
}
