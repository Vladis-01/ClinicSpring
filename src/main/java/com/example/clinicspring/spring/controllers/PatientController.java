package com.example.clinicspring.spring.controllers;

import com.example.clinicspring.spring.DTO.PatientDto;
import com.example.clinicspring.spring.services.AppointmentService;
import com.example.clinicspring.spring.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/doctor/patients")
@RequiredArgsConstructor
@Transactional
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @GetMapping("/createPatient")
    public String createPatient() {
        return "createPatient";
    }

    @GetMapping()
    public String getAllPatients(HashMap<String, Object> model) {
        model.put("patients", patientService.getAllPatients());
        return "patients";
    }

    @PostMapping("/createPatient")
    public String createPatient(@Valid PatientDto patient, BindingResult bindingResult) {
        PatientDto userFromDb = patientService.getPatientByUsername(patient.getUsername());

        if (userFromDb != null || bindingResult.hasErrors()) {
            return "redirect:/doctor/patients/createPatient";
        }


        patientService.createOrUpdatePatient(patient);

        return "redirect:/doctor/patients";
    }

    @GetMapping("{patientId}")
    public String editPatient(@PathVariable Long patientId, HashMap<String, Object> model){
        model.put("patient", patientService.getPatientById(patientId));
        model.put("appoiments", appointmentService.getAppointmentByPatientOrderByDateAppointment(patientService.getPatientById(patientId)));
        return "editPatientForDoctor";
    }

    @PostMapping
    public String editPatient(@Valid PatientDto patient, BindingResult bindingResult) {
        PatientDto userFromDb = patientService.getPatientByUsername(patient.getUsername());

        if (userFromDb != null && userFromDb.getId() != patient.getId() || bindingResult.hasErrors()) {
            return "redirect:/doctor/patients/{patient}";
        }
        patientService.createOrUpdatePatient(patient);
        return "redirect:/doctor/patients";
    }

    @DeleteMapping("/{patientId}")
    public String deleteFolder(@PathVariable Long patientId) {
        patientService.deletePatientById(patientId);
        return "redirect:/doctor/patients";
    }
}
