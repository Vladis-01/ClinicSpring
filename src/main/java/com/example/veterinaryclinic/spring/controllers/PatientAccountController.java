package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Position;
import com.example.veterinaryclinic.spring.entities.Doctor;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/patient")
public class PatientAccountController {
    private final PatientRepo patientRepo;
    private final AppoimentRepo appoimentRepo;
    private final DoctorRepo doctorRepo;
    private final PasswordEncoder passwordEncoder;

    public PatientAccountController(PatientRepo patientRepo, AppoimentRepo appoimentRepo, DoctorRepo doctorRepo, PasswordEncoder passwordEncoder) {
        this.patientRepo = patientRepo;
        this.appoimentRepo = appoimentRepo;
        this.doctorRepo = doctorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/editPatient")
    public String editPatient(@AuthenticationPrincipal Patient currentPatient, HashMap<String, Object> model) {
        model.put("patientModel", currentPatient);
        return "editPatient";
    }
    @PostMapping("/editPatient")
    public String editPatient(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
                              @AuthenticationPrincipal Patient currentPatient, @Valid Patient patient, BindingResult bindingResult) {

        if(!passwordEncoder.matches(currentPassword, currentPatient.getPassword()) || bindingResult.hasErrors()){
            return "redirect:/patient/editPatient";
        }

        if(!newPassword.equals("")){
            patient.setPassword(newPassword);
        }

        patientRepo.save(patient);
        return "patientAccount";
    }

    @GetMapping()
    public String getPatientAccount(@AuthenticationPrincipal Patient currentPatient, HashMap<String, Object> model) {
        if(currentPatient.getPassword().equals("")){
            return "redirect:/patient/editPatient";
        }
        model.put("patientModel", currentPatient);
        model.put("appoimentModels", appoimentRepo.findByPatientOrderByDateAppointment(currentPatient));

        return "patientAccount";
    }

    @GetMapping("/doctors")
    public String getDoctors(HashMap<String, Object> model, @RequestParam Map<String, String> form) {
        List<Doctor> doctors;
        if (form.get("position") != null && !form.get("position").equals("")) {
            Position position = Position.valueOf(form.get("position"));
            doctors = doctorRepo.findByPosition(position);
        } else {
            doctors = doctorRepo.findAll();
        }

        model.put("doctors", doctors);
        model.put("positions", Position.values());
        return "doctorsForPatient";
    }
}
