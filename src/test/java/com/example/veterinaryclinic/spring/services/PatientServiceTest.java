package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.enums.Role;
import com.example.veterinaryclinic.spring.mappings.MappingPatients;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PatientServiceTest {
    @Autowired
    private PatientService patientService;

    @MockBean
    private MappingPatients mappingPatients;

    @MockBean
    private PatientRepo patientRepo;

    @Test
    void createOrUpdatePatient() {
        PatientDto patientDto = new PatientDto();

        patientService.createOrUpdatePatient(patientDto);

        Assert.assertNotNull(patientDto.getDateRegistration());
        Assert.assertEquals(patientDto.getRole(), Collections.singleton(Role.USER));

        Patient patientEntity = Mockito.verify(mappingPatients, Mockito.times(1)).mapToPatientEntity(patientDto);
        Mockito.verify(patientRepo, Mockito.times(1)).save(patientEntity);
    }
}