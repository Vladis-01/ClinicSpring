package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.Enums.Status;
import com.example.veterinaryclinic.spring.entities.Appointment;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import com.example.veterinaryclinic.spring.services.AppointmentService;
import com.example.veterinaryclinic.spring.services.DoctorService;
import com.example.veterinaryclinic.spring.services.PatientService;
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
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppoimentController(AppointmentService appointmentService, DoctorService doctorService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
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

    @GetMapping("{appointmentId}")
    public String editAppointment(@PathVariable Long appointmentId, HashMap<String, Object> model){
        AppointmentDto appointment = appointmentService.getAppointmentById(appointmentId);

        HashMap statuses = (HashMap) Stream.of(Status.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(appointment.getStatus() != null) {
            for(Status status: appointment.getStatus()){
                statuses.replace(status, true);
            }
        }
        HashMap doctors = (HashMap) Stream.of(doctorService.getAllDoctors().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appointment.getDoctorDto() != null) {
            doctors.replace(appointment.getDoctorDto(), true);
        }
        HashMap patients = (HashMap) Stream.of(patientService.getAllPatients().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appointment.getPatientDto() != null) {
            patients.replace(appointment.getPatientDto(), true);
        }

        model.put("statuses", statuses);
        model.put("doctors", doctors);
        model.put("patients", patients);
        model.put("appoiment", appointment);
        return "editAppoiment";
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
            return "redirect:/doctor/editAppoiments";
        }

        appointmentService.createOrUpdateAppointment(appointment);
        return "redirect:/doctor/appoiments";
    }

    @DeleteMapping("/{appointmentId}")
    public String deleteFolder(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return "redirect:/doctor/appoiments";
    }
}
