package com.example.veterinaryclinic.spring.services;

import com.example.veterinaryclinic.spring.DTO.AppointmentDto;
import com.example.veterinaryclinic.spring.enums.Status;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.flywaydb.core.internal.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class StatusSchedule {
    private final AppointmentService appointmentService;

    public StatusSchedule(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @SchedulerLock(name = "task_lock", lockAtLeastFor = "PT10M")
    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void checkStatusAppoiment() throws InterruptedException {
        Instant before = Instant.now().minus(Duration.ofHours(1));
        Date dateMinus1Hour = Date.from(before);

        List<AppointmentDto> appointmentList = appointmentService.getAppointmentsByDateAppointmentBeforeByStatus(dateMinus1Hour, Status.NEW);
        for (AppointmentDto appointmentDto: appointmentList) {
            appointmentDto.setStatus(Collections.singleton(Status.CANCELED));
            appointmentService.createOrUpdateAppointment(appointmentDto);
        }
        appointmentService.createOrUpdateAppointments(appointmentList);
    }
}
