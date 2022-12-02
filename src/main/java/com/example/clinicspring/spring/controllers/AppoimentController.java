package com.example.clinicspring.spring.controllers;

import com.example.clinicspring.spring.DTO.AppointmentDto;
import com.example.clinicspring.spring.DTO.MedicineDto;
import com.example.clinicspring.spring.enums.Status;
import com.example.clinicspring.spring.services.AppointmentService;
import com.example.clinicspring.spring.services.DoctorService;
import com.example.clinicspring.spring.services.MedicineService;
import com.example.clinicspring.spring.services.PatientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/doctor/appoiments")
@Controller
public class AppoimentController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    private final MedicineService medicineService;

    @Value("${rlsr.password}")
    private String password;
    @Value("${rlsr.urlGetInventory}")
    private String urlRlsrGetInventory;

    public AppoimentController(AppointmentService appointmentService, DoctorService doctorService, PatientService patientService, MedicineService medicineService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.medicineService = medicineService;
    }

    @GetMapping()
    public String getAllAppoiments(HashMap<String, Object> model) {
        model.put("appoiments", appointmentService.getAllAppointments());
        return "appoiments";
    }

    @GetMapping("/createAppoiment")
    public String createAppiment(HashMap<String, Object> model) {
        model.put("appoiments", appointmentService.getAllAppointments());
        model.put("doctors", doctorService.getAllDoctors());
        model.put("patients", patientService.getAllPatients());
        model.put("statuses", Status.values());
        return "createAppoiment";
    }

    @PostMapping("/createAppoiment")
    public String createAppoiment(@Valid AppointmentDto appointment, @RequestParam Map<String, String> form) throws ParseException {
        appointment.setPatientDto(patientService.getPatientById(Long.valueOf(form.get("patient"))));
        appointment.setDoctorDto(doctorService.getDoctorById(Long.valueOf(form.get("doctor"))));
        appointment.setStatus(Collections.singleton(Status.valueOf(form.get("status"))));
        appointment.setMedicinesDto(new ArrayList<>());

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date = formatter.parse(form.get("date"));
        appointment.setDateAppointment(date);

        AppointmentDto appoimentFromDb = appointmentService.getAppointmentsByDateAppointmentAndDoctorOrDateAppointmentAndPatient(appointment.getDateAppointment(),
                appointment.getDoctorDto(), appointment.getDateAppointment(), appointment.getPatientDto());

        if (appoimentFromDb != null) {
            return "createAppoiment";
        }

        appointmentService.createOrUpdateAppointment(appointment);
        return "redirect:/doctor/appoiments";
    }

    @GetMapping(value = {"{appointmentId}"})
    public String editAppointment(@PathVariable Long appointmentId, @RequestParam(required = false) Map<String, String> form, HashMap<String, Object> model){

        AppointmentDto appointmentDto = appointmentService.getAppointmentById(appointmentId);

        HashMap statuses = (HashMap) Stream.of(Status.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(appointmentDto.getStatus() != null) {
            for(Status status: appointmentDto.getStatus()){
                statuses.replace(status, true);
            }
        }

        HashMap doctors = (HashMap) Stream.of(doctorService.getAllDoctors().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appointmentDto.getDoctorDto() != null) {
            doctors.replace(appointmentDto.getDoctorDto(), true);
        }

        HashMap patients = (HashMap) Stream.of(patientService.getAllPatients().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appointmentDto.getPatientDto() != null) {
            patients.replace(appointmentDto.getPatientDto(), true);
        }

        List<MedicineDto> medicinesDto = new ArrayList<>();
        for (MedicineDto medicineDto: appointmentDto.getMedicinesDto()) {
            MedicineDto medicineFromRLSR = searchMedicine(medicineDto.getPackingId());
            medicineFromRLSR.setValueId(medicineDto.getId());
            medicinesDto.add(medicineFromRLSR);
        }
        if(form.get("medicineName") != null && form.get("medicineName") != ""){
            model.put("medicinesFromRLSR", searchMedicines(form.get("medicineName")));
        }

        model.put("statuses", statuses);
        model.put("doctors", doctors);
        model.put("patients", patients);
        model.put("appoiment", appointmentDto);
        model.put("medicines", medicinesDto);

        return "editAppoiment";
    }

    @PostMapping("/{appointmentId}/addMedicine")
    public String addMedicine(@PathVariable Long appointmentId, @RequestParam Long medicinePackingId, HashMap<String, Object> model) throws ParseException {
        MedicineDto medicineDto = searchMedicine(medicinePackingId);

        AppointmentDto appointmentDto = appointmentService.getAppointmentById(appointmentId);
        if(medicineService.getMedicineByPackingIdAndAppointment(medicineDto.getPackingId(), appointmentDto) != null){
            return "redirect:/doctor/appoiments/{appointmentId}";
        }
        medicineDto.setValueAppointment(appointmentDto);
        medicineService.createOrUpdateMedicine(medicineDto);

        return "redirect:/doctor/appoiments/{appointmentId}";
    }

    @DeleteMapping("{appointmentId}/{medicineId}")
    public String deleteMedicine(@PathVariable Long appointmentId, @PathVariable Long medicineId) {
        medicineService.deleteMedicine(medicineId);
        return "redirect:/doctor/appoiments/{appointmentId}";
    }



    @PostMapping
    public String editAppoiment(@Valid AppointmentDto appointment, @RequestParam Map<String, String> form) throws ParseException {
        appointment.setPatientDto(patientService.getPatientById(Long.valueOf(form.get("patient"))));
        appointment.setDoctorDto(doctorService.getDoctorById(Long.valueOf(form.get("doctor"))));
        appointment.setStatus(Collections.singleton(Status.valueOf(form.get("status"))));

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date = formatter.parse(form.get("dateAppoiment"));
        appointment.setDateAppointment(date);

        AppointmentDto appoimentFromDb = appointmentService.getAppointmentsByDateAppointmentAndDoctorOrDateAppointmentAndPatient(appointment.getDateAppointment(),
                appointment.getDoctorDto(), appointment.getDateAppointment(), appointment.getPatientDto());

        if (appoimentFromDb != null) {
            return "redirect:/doctor/appoiments/" + appointment.getId();
        }

        appointmentService.createOrUpdateAppointment(appointment);
        return "redirect:/doctor/appoiments";
    }

    @DeleteMapping("/{appointmentId}")
    public String deleteFolder(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return "redirect:/doctor/appoiments";
    }

    @GetMapping("/{appointmentId}/medicine/{packingId}")
    public String openMedicine(@PathVariable Long appointmentId, @PathVariable Long packingId, HashMap<String, Object> model) {
        MedicineDto medicineFromRLSR = searchMedicine(packingId);
        model.put("medicine", medicineFromRLSR);
        model.put("apppointmentId", appointmentId);

        return "medicineForDoctor";
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
