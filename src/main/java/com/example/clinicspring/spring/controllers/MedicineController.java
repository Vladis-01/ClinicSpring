package com.example.clinicspring.spring.controllers;

import com.example.clinicspring.spring.DTO.MedicineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Controller
public class MedicineController {
    @Value("${rlsr.password}")
    private String password;
    @Value("${rlsr.urlGetInventory}")
    private String urlRlsrGetInventory;

    @GetMapping("/doctor/medicine")
    public List<MedicineDto> getAllDoctors(HashMap<String, Object> model) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", password);
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<MedicineDto>> response = restTemplate.exchange(urlRlsrGetInventory + "?tn_like=Про", //
                HttpMethod.GET, request, new ParameterizedTypeReference<List<MedicineDto>>(){});

        System.out.println(response);
        return response.getBody();
    }
}
