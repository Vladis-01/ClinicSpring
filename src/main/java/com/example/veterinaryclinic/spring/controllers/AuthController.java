package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

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
        if(doctorRepo.findAll().size() == 0){
            DoctorModel doctorModel = new DoctorModel();
            doctorModel.setUsername("admin");
            doctorModel.setPassword("admin");
            doctorModel.setFullName("admin");
            doctorModel.setRole(new HashSet<>(Collections.singleton(Role.ADMIN)));
            doctorRepo.save(doctorModel);
        }
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

    @GetMapping("/checkUser")
    public String checkUser(@AuthenticationPrincipal DoctorModel currentDoctor, @AuthenticationPrincipal PatientModel currentPatient){
        if(currentDoctor != null){
            return "/doctor";
        }
        if(currentPatient != null){
            return "/patient";
        }
        return "/";
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