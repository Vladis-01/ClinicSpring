package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/doctor/patients")
@Transactional
public class PatientController {
    private final PatientRepo patientRepo;
    private final AppoimentRepo appoimentRepo;

    public PatientController(PatientRepo patientRepo, AppoimentRepo appoimentRepo) {
        this.patientRepo = patientRepo;
        this.appoimentRepo = appoimentRepo;
    }

    @GetMapping("/createPatient")
    public String createPatient() {
        return "createPatient";
    }

    @GetMapping()
    public String getAllPatients(HashMap<String, Object> model) {
        model.put("patients", patientRepo.findAll());
        return "patients";
    }

    @PostMapping("/createPatient")
    public String createPatient(@Valid Patient patient) {
        Patient userFromDb = patientRepo.findByUsername(patient.getUsername()).orElse(null);

        if (userFromDb != null) {
            return "redirect:/doctor/patients/createPatient";
        }

        patient.setDateRegistration(new Date());
        patient.setPassword("");
        patient.setRole(Collections.singleton(Role.USER));
        patientRepo.save(patient);

        return "redirect:/doctor/patients";
    }

    @GetMapping("{patient}")
    public String editPatient(@PathVariable Patient patient, HashMap<String, Object> model){
        model.put("appoiments", appoimentRepo.findByPatientOrderByDateAppointment(patient));
        return "editPatientForDoctor";
    }

    @PostMapping
    public String editPatient(@Valid Patient patient, @RequestParam Map<String, String> form) {
        Patient userFromDb = patientRepo.findByUsername(patient.getUsername()).orElse(null);

        if (userFromDb != null && userFromDb.getId() != patient.getId()) {
            return "redirect:/doctor/patients/{patient}";
        }
        patientRepo.save(patient);
        return "redirect:/doctor/patients";
    }

    @DeleteMapping("/{patient}")
    public String deleteFolder(@PathVariable Patient patient) {
        patientRepo.delete(patient);
        return "redirect:/doctor/patients";
    }
}
