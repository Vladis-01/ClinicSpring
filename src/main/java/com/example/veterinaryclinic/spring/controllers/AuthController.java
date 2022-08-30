package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.enums.Role;
import com.example.veterinaryclinic.spring.entities.Doctor;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.services.PatientService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

@Controller
public class AuthController {
    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(PatientService patientService, PasswordEncoder passwordEncoder) {
        this.patientService = patientService;
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
    public String registrationPatient(@Valid PatientDto patient, BindingResult bindingResult) {
        PatientDto userFromDb = patientService.getPatientByUsername(patient.getUsername());

        if (userFromDb != null || bindingResult.hasErrors()) {
            return "redirect:/registration";
        }
        patient.setDateRegistration(new Date());
        patient.setRole(Collections.singleton(Role.USER));
        patientService.createOrUpdatePatient(patient);

        return "redirect:/login";
    }
}