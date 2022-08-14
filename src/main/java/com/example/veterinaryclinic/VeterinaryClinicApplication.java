package com.example.veterinaryclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.veterinaryclinic.spring")
public class VeterinaryClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(VeterinaryClinicApplication.class, args);
    }

}
