package com.example.clinicspring.spring.controllers;

import com.example.clinicspring.spring.DTO.AppointmentDto;
import com.example.clinicspring.spring.DTO.DoctorDto;
import com.example.clinicspring.spring.DTO.MedicineDto;
import com.example.clinicspring.spring.DTO.PatientDto;
import com.example.clinicspring.spring.enums.Position;
import com.example.clinicspring.spring.entities.Patient;
import com.example.clinicspring.spring.enums.Status;
import com.example.clinicspring.spring.services.AppointmentService;
import com.example.clinicspring.spring.services.DoctorService;
import com.example.clinicspring.spring.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientAccountController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PasswordEncoder passwordEncoder;

    @Value("${rlsr.password}")
    private String password;

    @Value("${rlsr.urlGetInventory}")
    private String urlRlsrGetInventory;

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

    @GetMapping(value = {"/appoiment/{appointmentId}"})
    public String openAppointment(@PathVariable Long appointmentId, HashMap<String, Object> model){

        AppointmentDto appointmentDto = appointmentService.getAppointmentById(appointmentId);

        List<MedicineDto> medicinesDto = new ArrayList<>();
        for (MedicineDto medicineDto: appointmentDto.getMedicinesDto()) {
            MedicineDto medicineFromRLSR = searchMedicine(medicineDto.getPackingId());
            medicineFromRLSR.setValueId(medicineDto.getId());
            medicinesDto.add(medicineFromRLSR);
        }

        model.put("status", appointmentDto.getStatus());
        model.put("doctor", appointmentDto.getDoctorDto().getFullName());
        model.put("patient", appointmentDto.getPatientDto().getFullName());
        model.put("appoiment", appointmentDto);
        model.put("medicines", medicinesDto);

        return "appoimentForPatient";
    }
    @GetMapping("/appoiment/{appointmentId}/medicine/{packingId}")
    public String openMedicine(@PathVariable Long appointmentId, @PathVariable Long packingId, HashMap<String, Object> model) {
        MedicineDto medicineFromRLSR = searchMedicine(packingId);
        model.put("medicine", medicineFromRLSR);
        model.put("appointmentId", appointmentId);

        return "medicineForPatient";
    }


    public List<MedicineDto> searchMedicines(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", password);
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<List<MedicineDto>> response = restTemplate.exchange(urlRlsrGetInventory + "?tn_like=" + name, //
                    HttpMethod.GET, request, new ParameterizedTypeReference<List<MedicineDto>>() {});
            List<MedicineDto> medicinesDto = response.getBody();

            return medicinesDto.stream().filter(med -> med.getType().equals("Лекарственные средства")).toList();
        } catch (Exception e){
            return new ArrayList<MedicineDto>();
        }
    }

    public MedicineDto searchMedicine(Long packingId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", password);
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MedicineDto> response = restTemplate.exchange(urlRlsrGetInventory + "?packing_id=" + packingId, //
                HttpMethod.GET, request, MedicineDto.class);
        MedicineDto medicineDto = response.getBody();

        return medicineDto;
    }
}
