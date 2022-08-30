package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.DTO.DoctorDto;
import com.example.veterinaryclinic.spring.enums.Position;
import com.example.veterinaryclinic.spring.enums.Role;
import com.example.veterinaryclinic.spring.services.DoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("/doctor/doctors")
@Controller
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/createDoctor")
    public String createDoctor(HashMap<String, Object> model) {
        model.put("positions", Position.values());
        model.put("roles", Role.values());
        return "createDoctor";
    }

    @GetMapping()
    public String getAllDoctors(HashMap<String, Object> model) {
        model.put("doctors", doctorService.getAllDoctors());
        return "doctors";
    }

    @GetMapping("{doctorId}")
    public String editDoctor(@PathVariable Long doctorId, HashMap<String, Object> model){
        DoctorDto doctor = doctorService.getDoctorById(doctorId);
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

        model.put("doctor", doctor);
        model.put("positions", positions);
        model.put("roles", roles);

        return "editDoctor";
    }

    @PostMapping
    public String editDoctor(@Valid DoctorDto doctor, BindingResult bindingResult, @RequestParam Map<String, String> form) {
        DoctorDto userFromDb = doctorService.getDoctorByUsername(doctor.getUsername());

        if (userFromDb != null && userFromDb.getId() != doctor.getId() || bindingResult.hasErrors()) {
            return "redirect:/doctor/doctors/{doctor}";
        }

        String newPassword = form.get("newPassword");
        if(!newPassword.equals("")){
            doctor.setPassword(userFromDb.getPassword());
        }else doctor.setPassword(newPassword);

        doctorService.createOrUpdateDoctor(doctor, form);
        return "redirect:/doctor/doctors";
    }

    @PostMapping("/createDoctor")
    public String createDoctor(@Valid DoctorDto doctor, BindingResult bindingResult, @RequestParam Map<String, String> form) {
        DoctorDto userFromDb = doctorService.getDoctorByUsername(doctor.getUsername());

        if (userFromDb != null || bindingResult.hasErrors()) {
            return "redirect:/doctor/doctors/createDoctor";
        }
        doctorService.createOrUpdateDoctor(doctor, form);

        return "redirect:/doctor/doctors";
    }

    @DeleteMapping("/{doctorId}")
    public String deleteFolder(@PathVariable Long doctorId) {
        doctorService.deleteDoctorById(doctorId);
        return "redirect:/doctor/doctors";
    }
}
