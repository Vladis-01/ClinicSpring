package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Position;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/patient")
public class PatientAccountController {
    final
    PatientRepo patientRepo;
    final
    AppoimentRepo appoimentRepo;

    final
    DoctorRepo doctorRepo;

    @Autowired
    public PatientAccountController(PatientRepo patientRepo, AppoimentRepo appoimentRepo, DoctorRepo doctorRepo) {
        this.patientRepo = patientRepo;
        this.appoimentRepo = appoimentRepo;
        this.doctorRepo = doctorRepo;
    }

    @GetMapping("/editPatient")
    public String editPatient(@AuthenticationPrincipal PatientModel currentPatient, HashMap<String, Object> model) {
        model.put("patientModel", currentPatient);
        return "editPatient";
    }
    @PostMapping("/editPatient")
    public String editPatient(@AuthenticationPrincipal PatientModel currentPatient, PatientModel patientModel) {
        currentPatient.setPassword(patientModel.getPassword());
        if(currentPatient.getPassword().equals("")){
            return "redirect:/patient/editPatient";
        }

        patientRepo.save(currentPatient);
        return "patientAccount";
    }

    @GetMapping()
    public String getPatientAccount(@AuthenticationPrincipal PatientModel currentPatient, HashMap<String, Object> model) {
        if(currentPatient.getPassword().equals("")){
            return "redirect:/patient/editPatient";
        }
        model.put("patientModel", currentPatient);
        model.put("appoimentModels", appoimentRepo.findByPatientModelOrderByDateAppointment(currentPatient));

        return "patientAccount";
    }

    @GetMapping("/doctors")
    public String getDoctors(HashMap<String, Object> model, @RequestParam Map<String, String> form) {
        List<DoctorModel> doctors;
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
