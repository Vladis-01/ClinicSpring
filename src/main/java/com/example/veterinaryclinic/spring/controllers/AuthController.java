package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@Controller
public class AuthController {
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AppoimentRepo appoimentRepo;

    @Autowired
    public AuthController(DoctorRepo doctorRepo, PatientRepo patientRepo, AppoimentRepo appoimentRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.appoimentRepo = appoimentRepo;
    }

    @GetMapping("/doctor/registration")
    public String registrationDoctor() {
        return "registrationDoctor";
    }

    @GetMapping("/doctor/login")
    public String loginDoctor() {
        return "loginDoctor";
    }

    @GetMapping("/doctor")
    public String getDoctorAccount(@AuthenticationPrincipal DoctorModel currentDoctor, HashMap<String, Object> model) {
        model.put("doctorModel", currentDoctor);
        return "doctorAccount";
    }

    @GetMapping("/patient/registration")
    public String registrationPatient() {
        return "registrationPatient";
    }

    @GetMapping("/patient/login")
    public String loginPatient() {
        return "loginPatient";
    }



    @PostMapping("/patient/registration")
    public String registrationPatient(PatientModel patientModel) {
        PatientModel userFromDb = patientRepo.findByUsername(patientModel.getUsername());

        if (userFromDb != null) {
            return "redirect:/patient/registration";
        }
        patientModel.setDateRegistration(new Date());
        patientRepo.save(patientModel);

        return "redirect:/patient/login";
    }
}