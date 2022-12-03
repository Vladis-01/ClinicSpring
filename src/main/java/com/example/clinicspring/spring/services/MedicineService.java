package com.example.clinicspring.spring.services;

import com.example.clinicspring.spring.DTO.AppointmentDto;
import com.example.clinicspring.spring.DTO.MedicineDto;
import com.example.clinicspring.spring.mappings.MappingAppointment;
import com.example.clinicspring.spring.mappings.MappingMedicines;
import com.example.clinicspring.spring.repositories.MedicineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepo medicineRepo;
    private final MappingMedicines mappingMedicines;

    private final MappingAppointment mappingAppointment;

    @Transactional
    public void createOrUpdateMedicine(MedicineDto medicineDto){
        medicineRepo.save(mappingMedicines.mapToMedicineEntity(medicineDto));
    }

    public void deleteMedicine(MedicineDto medicineDto){
        medicineRepo.delete(mappingMedicines.mapToMedicineEntity(medicineDto));
    }

    public void deleteMedicine(Long id){
        medicineRepo.deleteById(id);
        medicineRepo.flush();
    }

    public MedicineDto getMedicineByPackingIdAndAppointment(Long packingId, AppointmentDto appointmentDto){
        return mappingMedicines.mapToMedicineDto(medicineRepo.findByPackingIdAndAppointment(packingId, mappingAppointment.mapToAppointmentEntity(appointmentDto)));
    }
}
