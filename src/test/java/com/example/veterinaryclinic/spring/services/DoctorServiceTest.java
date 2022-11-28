package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.DoctorDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.entities.Doctor;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.enums.Position;
import com.example.veterinaryclinic.spring.enums.Role;
import com.example.veterinaryclinic.spring.mappings.MappingDoctors;
import com.example.veterinaryclinic.spring.mappings.MappingPatients;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DoctorServiceTest {
    @Autowired
    private DoctorService doctorService;

    @MockBean
    private MappingDoctors mappingDoctors;

    @MockBean
    private DoctorRepo doctorRepo;

    @Test
    void createOrUpdateDoctor() {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setPassword("");

        Map<String, String> form = new HashMap();
        form.put("THERAPIST", "on");
        form.put("ENT", "on");
        form.put("DOCTOR", "on");

        doctorService.createOrUpdateDoctor(doctorDto, form);

        Assert.assertEquals(doctorDto.getPosition(), Set.of(Position.THERAPIST, Position.ENT));
        Assert.assertEquals(doctorDto.getRole(), Set.of(Role.DOCTOR));

        Doctor doctorEntity = Mockito.verify(mappingDoctors, Mockito.times(1)).mapToDoctorEntity(doctorDto);
        Mockito.verify(doctorRepo, Mockito.times(1)).save(doctorEntity);
    }
}