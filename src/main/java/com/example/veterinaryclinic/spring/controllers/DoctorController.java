package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Position;
import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("/doctor/doctors")
@Controller
public class DoctorController {
    private final DoctorRepo doctorRepo;
    private final DoctorService doctorService;

    public DoctorController(DoctorRepo doctorRepo, DoctorService doctorService) {
        this.doctorRepo = doctorRepo;
        this.doctorService = doctorService;
    }

    @GetMapping("/createDoctor")
    public String createDoctor(DoctorModel doctorModel, HashMap<String, Object> model) {
        model.put("positions", Position.values());
        model.put("roles", Role.values());
        return "createDoctor";
    }

    @GetMapping()
    public String getAllDoctors(HashMap<String, Object> model) {
        model.put("doctors", doctorRepo.findAll());
        return "doctors";
    }

    @GetMapping("{doctorModel}")
    public String editDoctor(@PathVariable DoctorModel doctorModel, HashMap<String, Object> model){
        HashMap roles = (HashMap) Stream.of(Role.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(doctorModel.getRole() != null) {
            for(Role role: doctorModel.getRole()){
                roles.put(role, true);
            }
        }
        HashMap positions = (HashMap) Stream.of(Position.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(doctorModel.getRole() != null) {
            for(Position position: doctorModel.getPosition()){
                positions.put(position, true);
            }
        }

        model.put("positions", positions);
        model.put("roles", roles);

        return "editDoctor";
    }

    @PostMapping
    public String editDoctor(DoctorModel doctorModel, @RequestParam Map<String, String> form) {
        DoctorModel userFromDb = doctorRepo.findByUsername(doctorModel.getUsername());

        if (userFromDb != null && userFromDb.getId() != doctorModel.getId()) {
            return "redirect:/doctor/doctors/{doctorModel}";
        }

        doctorService.createOrUpdateDoctor(doctorModel, form);
        return "redirect:/doctor/doctors";
    }

    @PostMapping("/createDoctor")
    public String createDoctor(DoctorModel doctorModel, @RequestParam Map<String, String> form) {
        DoctorModel userFromDb = doctorRepo.findByUsername(doctorModel.getUsername());

        if (userFromDb != null) {
            return "redirect:/doctor/doctors/createDoctor";
        }
        doctorService.createOrUpdateDoctor(doctorModel, form);

        return "redirect:/doctor/doctors";
    }

    @DeleteMapping("/{doctor}")
    public String deleteFolder(@PathVariable DoctorModel doctor) {
        doctorRepo.delete(doctor);
        return "redirect:/doctor/doctors";
    }
}
