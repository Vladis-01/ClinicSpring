package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.Enums.Position;
import com.example.veterinaryclinic.spring.Enums.Role;
import com.example.veterinaryclinic.spring.entities.Doctor;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.services.DoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public String createDoctor(Doctor doctor, HashMap<String, Object> model) {
        model.put("positions", Position.values());
        model.put("roles", Role.values());
        return "createDoctor";
    }

    @GetMapping()
    public String getAllDoctors(HashMap<String, Object> model) {
        model.put("doctors", doctorRepo.findAll());
        return "doctors";
    }

    @GetMapping("{doctor}")
    public String editDoctor(@PathVariable Doctor doctor, HashMap<String, Object> model){
        HashMap roles = (HashMap) Stream.of(Role.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(doctor.getRole() != null) {
            for(Role role: doctor.getRole()){
                roles.put(role, true);
            }
        }
        HashMap positions = (HashMap) Stream.of(Position.values()).collect(Collectors.toMap(e -> e, e -> false));
        if(doctor.getRole() != null) {
            for(Position position: doctor.getPosition()){
                positions.put(position, true);
            }
        }

        model.put("positions", positions);
        model.put("roles", roles);

        return "editDoctor";
    }

    @PostMapping
    public String editDoctor(@Valid Doctor doctor, BindingResult bindingResult, @RequestParam Map<String, String> form) {
        Doctor userFromDb = doctorRepo.findByUsername(doctor.getUsername()).orElse(null);

        if (userFromDb != null && userFromDb.getId() != doctor.getId() || bindingResult.hasErrors()) {
            return "redirect:/doctor/doctors/{doctor}";
        }

        String newPassword = form.get("newPassword");
        if(newPassword.equals("")){
            doctor.setPassword(userFromDb.getPassword());
        }else doctor.setPassword(newPassword);

        doctorService.createOrUpdateDoctor(doctor, form);
        return "redirect:/doctor/doctors";
    }

    @PostMapping("/createDoctor")
    public String createDoctor(@Valid Doctor doctor, BindingResult bindingResult, @RequestParam Map<String, String> form) {
        Doctor userFromDb = doctorRepo.findByUsername(doctor.getUsername()).orElse(null);

        if (userFromDb != null || bindingResult.hasErrors()) {
            return "redirect:/doctor/doctors/createDoctor";
        }
        doctorService.createOrUpdateDoctor(doctor, form);

        return "redirect:/doctor/doctors";
    }

    @DeleteMapping("/{doctor}")
    public String deleteFolder(@PathVariable Doctor doctor) {
        doctorRepo.delete(doctor);
        return "redirect:/doctor/doctors";
    }
}
