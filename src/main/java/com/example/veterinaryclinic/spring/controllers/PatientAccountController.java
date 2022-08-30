package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.DTO.DoctorDto;
import com.example.veterinaryclinic.spring.DTO.PatientDto;
import com.example.veterinaryclinic.spring.enums.Position;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.services.AppointmentService;
import com.example.veterinaryclinic.spring.services.DoctorService;
import com.example.veterinaryclinic.spring.services.PatientService;
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
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PasswordEncoder passwordEncoder;

    public PatientAccountController(PatientService patientService, AppointmentService appointmentService, DoctorService doctorService, PasswordEncoder passwordEncoder) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/editPatient")
    public String editPatient(@AuthenticationPrincipal Patient currentPatient, HashMap<String, Object> model) {
        model.put("patient", currentPatient);
        return "editPatient";
    }
    @PostMapping("/editPatient")
    public String editPatient(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
                              @AuthenticationPrincipal Patient currentPatient, @Valid PatientDto patient, BindingResult bindingResult) {

        if(!passwordEncoder.matches(currentPassword, currentPatient.getPassword()) || bindingResult.hasErrors()){
            return "redirect:/patient/editPatient";
        }
        patient.setDateRegistration(currentPatient.getDateRegistration());
        patient.setPassword(currentPatient.getPassword());

        if(!newPassword.equals("")){
            patient.setPassword(passwordEncoder.encode(newPassword));
        }

        patientService.createOrUpdatePatient(patient);
        return "redirect:/patient";
    }

    @GetMapping()
    public String getPatientAccount(@AuthenticationPrincipal Patient currentPatient, HashMap<String, Object> model) {
        PatientDto patient = patientService.getPatientById(currentPatient.getId());
        if(passwordEncoder.matches("",patient.getPassword())){ // пустой пароль
            return "redirect:/patient/editPatient";
        }
        model.put("patient", patient);
        model.put("appoiments", appointmentService.getAppointmentByPatientOrderByDateAppointment(patientService.getPatientById(currentPatient.getId())));

        return "patientAccount";
    }

    @GetMapping("/doctors")
    public String getDoctors(HashMap<String, Object> model, @RequestParam Map<String, String> form) {
        List<DoctorDto> doctors;
        if (form.get("position") != null && !form.get("position").equals("")) {
            Position position = Position.valueOf(form.get("position"));
            doctors = doctorService.getDoctorByPosition(position);
        } else {
            doctors = doctorService.getAllDoctors().stream().toList();
        }

        model.put("doctors", doctors);
        model.put("positions", Position.values());
        return "doctorsForPatient";
    }
}
