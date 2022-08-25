package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Status;
import com.example.veterinaryclinic.spring.entities.Appointment;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private final AppoimentRepo appoimentRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;

    public AppoimentController(AppoimentRepo appoimentRepo, DoctorRepo doctorRepo, PatientRepo patientRepo) {
        this.appoimentRepo = appoimentRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    @GetMapping()
    public String getAllAppoiments(HashMap<String, Object> model) {
        model.put("appoiments", appoimentRepo.findAll());
        return "appoiments";
    }

    @GetMapping("/createAppoiment")
    public String createAppiment(HashMap<String, Object> model) {
        model.put("appoiments", appoimentRepo.findAll());
        model.put("doctors", doctorRepo.findAll());
        model.put("patients", patientRepo.findAll());
        model.put("statuses", Status.values());
        return "createAppoiment";
    }

    @PostMapping("/createAppoiment")
    public String createAppoiment(@Valid Appointment appointment, @RequestParam Map<String, String> form) throws ParseException {
        Appointment appoimentFromDb = appoimentRepo.findByDateAppointmentAndDoctorOrDateAppointmentAndPatient(appointment.getDateAppointment(),
                appointment.getDoctor(), appointment.getDateAppointment(), appointment.getPatient());

        if (appoimentFromDb != null) {
            return "createAppoiment";
        }

        appointment.setPatient(patientRepo.getReferenceById(Long.valueOf(form.get("patient"))));
        appointment.setDoctor(doctorRepo.getReferenceById(Long.valueOf(form.get("doctor"))));
        appointment.setStatus(Collections.singleton(Status.valueOf(form.get("status"))));

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date = formatter.parse(form.get("date"));
        appointment.setDateAppointment(date);

        appoimentRepo.save(appointment);
        return "redirect:/doctor/appoiments";
    }

    @GetMapping("{appoiment}")
    public String editDoctor(@PathVariable Appointment appoiment, HashMap<String, Object> model){
        HashMap statuses = (HashMap) Stream.of(Status.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(appoiment.getStatus() != null) {
            for(Status status: appoiment.getStatus()){
                statuses.put(status, true);
            }
        }
        HashMap doctors = (HashMap) Stream.of(doctorRepo.findAll().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appoiment.getDoctor() != null) {
            doctors.put(appoiment.getDoctor(), true);
        }
        HashMap patients = (HashMap) Stream.of(patientRepo.findAll().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appoiment.getPatient() != null) {
            patients.put(appoiment.getPatient(), true);
        }

        model.put("statuses", statuses);
        model.put("doctors", doctors);
        model.put("patients", patients);
        return "editAppoiment";
    }

    @PostMapping
    public String editAppoiment(@Valid Appointment appointment, @RequestParam Map<String, String> form) throws ParseException {
        Appointment appoimentFromDb = appoimentRepo.findByDateAppointmentAndDoctorOrDateAppointmentAndPatient(appointment.getDateAppointment(),
                appointment.getDoctor(), appointment.getDateAppointment(), appointment.getPatient());

        if (appoimentFromDb != null) {
            return "redirect:/doctor/editAppoiments";
        }

        appointment.setPatient(patientRepo.getReferenceById(Long.valueOf(form.get("patient"))));
        appointment.setDoctor(doctorRepo.getReferenceById(Long.valueOf(form.get("doctor"))));
        appointment.setStatus(Collections.singleton(Status.valueOf(form.get("status"))));

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date = formatter.parse(form.get("dateAppoiment"));
        appointment.setDateAppointment(date);

        appoimentRepo.save(appointment);
        return "redirect:/doctor/appoiments";
    }

    @DeleteMapping("/{appointment}")
    public String deleteFolder(@PathVariable Appointment appointment) {
        appoimentRepo.delete(appointment);
        return "redirect:/doctor/appoiments";
    }
}
