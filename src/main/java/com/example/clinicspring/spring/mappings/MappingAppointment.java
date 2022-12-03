package com.example.clinicspring.spring.mappings;

import com.example.clinicspring.spring.DTO.*;
import com.example.clinicspring.spring.entities.Appointment;
import com.example.clinicspring.spring.entities.Medicine;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MappingAppointment {
    private final ModelMapper modelMapper;
    private final MappingMedicines mappingMedicines;

    public MappingAppointment(ModelMapper modelMapper, MappingMedicines mappingMedicines) {
        this.modelMapper = modelMapper;
        this.mappingMedicines = mappingMedicines;
    }

    //из entity в dto
    public AppointmentDto mapToAppointmentDto(Appointment appointment){
        if(appointment==null){
            return null;
        }

        AppointmentDto appointmentDto = modelMapper.map(appointment, AppointmentDto.class);
        if(appointment.getDoctor() != null){
            appointmentDto.setDoctorDto(modelMapper.map(appointment.getDoctor(), DoctorDto.class));
        }
        if(appointment.getPatient() != null){
            appointmentDto.setPatientDto(modelMapper.map(appointment.getPatient(), PatientDto.class));
        }
        List<MedicineDto> medicineDtoList = new ArrayList<>();
        if(appointment.getMedicines() != null){
            for (Medicine medicine: appointment.getMedicines()) {
                MedicineDto medicineDto = new MedicineDto();
                medicineDto.setValueId(medicine.getId());
                medicineDto.setValuePackingId(medicine.getPackingId());
                medicineDtoList.add(medicineDto);
            }
            appointmentDto.setMedicinesDto(medicineDtoList);
        }

        return appointmentDto;
    }
    //из dto в entity
    public Appointment mapToAppointmentEntity(AppointmentDto dto){
        Appointment appointment = modelMapper.map(dto, Appointment.class);
        appointment.setStatus(dto.getStatus());

        return appointment;
    }
}
