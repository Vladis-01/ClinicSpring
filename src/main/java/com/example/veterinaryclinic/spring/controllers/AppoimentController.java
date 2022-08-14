package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Status;
import com.example.veterinaryclinic.spring.models.AppointmentModel;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.FolderModel;
import com.example.veterinaryclinic.spring.repositories.AppoimentRepo;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/doctor/appoiments")
@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
@Controller
public class AppoimentController {
    final
    AppoimentRepo appoimentRepo;
    final
    DoctorRepo doctorRepo;
    final PatientRepo patientRepo;

    @Autowired
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
    public String createAppoiment(AppointmentModel appointmentModel, @RequestParam Map<String, String> form) throws ParseException {
        AppointmentModel appoimentFromDb = appoimentRepo.findByDateAppointmentAndDoctorModelOrDateAppointmentAndPatientModel(appointmentModel.getDateAppointment(),
                appointmentModel.getDoctorModel(), appointmentModel.getDateAppointment(), appointmentModel.getPatientModel());

        if (appoimentFromDb != null) {
            return "createAppoiment";
        }

        appointmentModel.setPatientModel(patientRepo.getReferenceById(Long.valueOf(form.get("patientModel"))));
        appointmentModel.setDoctorModel(doctorRepo.getReferenceById(Long.valueOf(form.get("doctorModel"))));
        appointmentModel.setStatus(Collections.singleton(Status.valueOf(form.get("status"))));

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date = formatter.parse(form.get("date"));
        appointmentModel.setDateAppointment(date);

        appoimentRepo.save(appointmentModel);
        return "redirect:/doctor/appoiments";
    }

    @GetMapping("{appoimentModel}")
    public String editDoctor(@PathVariable AppointmentModel appoimentModel, HashMap<String, Object> model){
        HashMap statuses = (HashMap) Stream.of(Status.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(appoimentModel.getStatus() != null) {
            for(Status status: appoimentModel.getStatus()){
                statuses.put(status, true);
            }
        }
        HashMap doctors = (HashMap) Stream.of(doctorRepo.findAll().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appoimentModel.getDoctorModel() != null) {
            doctors.put(appoimentModel.getDoctorModel(), true);
        }
        HashMap patients = (HashMap) Stream.of(patientRepo.findAll().toArray()).collect(Collectors.toMap(e -> e, e -> false));
        if(appoimentModel.getPatientModel() != null) {
            patients.put(appoimentModel.getPatientModel(), true);
        }

        model.put("statuses", statuses);
        model.put("doctors", doctors);
        model.put("patients", patients);
        return "editAppoiment";
    }

    @PostMapping
    public String editAppoiment(AppointmentModel appointmentModel, @RequestParam Map<String, String> form) throws ParseException {
        AppointmentModel appoimentFromDb = appoimentRepo.findByDateAppointmentAndDoctorModelOrDateAppointmentAndPatientModel(appointmentModel.getDateAppointment(),
                appointmentModel.getDoctorModel(), appointmentModel.getDateAppointment(), appointmentModel.getPatientModel());

        if (appoimentFromDb != null) {
            return "redirect:/doctor/editAppoiments";
        }

        appointmentModel.setPatientModel(patientRepo.getReferenceById(Long.valueOf(form.get("patientModel"))));
        appointmentModel.setDoctorModel(doctorRepo.getReferenceById(Long.valueOf(form.get("doctorModel"))));
        appointmentModel.setStatus(Collections.singleton(Status.valueOf(form.get("status"))));

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date = formatter.parse(form.get("dateAppoiment"));
        appointmentModel.setDateAppointment(date);

        appoimentRepo.save(appointmentModel);
        return "redirect:/doctor/appoiments";
    }

    @DeleteMapping("/{appointmentModel}")
    public String deleteFolder(@PathVariable AppointmentModel appointmentModel) {
        appoimentRepo.delete(appointmentModel);
        return "redirect:/doctor/appoiments";
    }
}
