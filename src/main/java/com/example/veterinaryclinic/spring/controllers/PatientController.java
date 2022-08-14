package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.models.FolderModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/doctor/patients")
@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
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
    public String createPatient(PatientModel patientModel) {
        PatientModel userFromDb = patientRepo.findByUsername(patientModel.getUsername());

        if (userFromDb != null) {
            return "redirect:/doctor/patients/createPatient";
        }

        patientModel.setDateRegistration(new Date());
        patientModel.setPassword("");
        patientRepo.save(patientModel);

        return "redirect:/doctor/patients";
    }

    @GetMapping("{patientModel}")
    public String editPatient(@PathVariable PatientModel patientModel, HashMap<String, Object> model){
        model.put("appoiments", appoimentRepo.findByPatientModelOrderByDateAppointment(patientModel));
        return "editPatientForDoctor";
    }

    @PostMapping
    public String editPatient(PatientModel patientModel, @RequestParam Map<String, String> form) {
        PatientModel userFromDb = patientRepo.findByUsername(patientModel.getUsername());

        if (userFromDb != null && userFromDb.getId() != patientModel.getId()) {
            return "redirect:/doctor/patients/{patientModel}";
        }
        patientRepo.save(patientModel);
        return "redirect:/doctor/patients";
    }

    @DeleteMapping("/{patient}")
    public String deleteFolder(@PathVariable PatientModel patient) {
        patientRepo.delete(patient);
        return "redirect:/doctor/patients";
    }
}
