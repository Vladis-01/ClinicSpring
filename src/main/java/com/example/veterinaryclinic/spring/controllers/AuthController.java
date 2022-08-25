package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.entities.Doctor;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

@Controller
public class AuthController {
    private final PatientRepo patientRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(PatientRepo patientRepo, PasswordEncoder passwordEncoder) {
        this.patientRepo = patientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "loginDoctor";
    }

    @GetMapping("/doctor")
    public String getDoctorAccount(@AuthenticationPrincipal Doctor currentDoctor, HashMap<String, Object> model) {
        model.put("doctor", currentDoctor);
        return "doctorAccount";
    }

    @GetMapping("/registration")
    public String registrationPatient() {
        return "registrationPatient";
    }


    @GetMapping("/checkUser")
    public String checkUser(@AuthenticationPrincipal Doctor currentDoctor, @AuthenticationPrincipal Patient currentPatient){
        if(currentDoctor != null){
            return "/doctor";
        }
        if(currentPatient != null){
            return "/patient";
        }
        return "/";
    }

    @PostMapping("/registration")
    public String registrationPatient(@Valid Patient patient) {
        Patient userFromDb = patientRepo.findByUsername(patient.getUsername()).orElse(null);

        if (userFromDb != null) {
            return "redirect:/registration";
        }
        patient.setDateRegistration(new Date());
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRole(Collections.singleton(Role.USER));
        patientRepo.save(patient);

        return "redirect:/login";
    }
}