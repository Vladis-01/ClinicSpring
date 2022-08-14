package com.example.veterinaryclinic.spring.repositories;

import com.example.veterinaryclinic.spring.models.AppointmentModel;
import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AppoimentRepo extends JpaRepository<AppointmentModel, Long> {
    // Фильтр для того чтобы узнать есть ли запись на выбранное время у пациента или доктора
    AppointmentModel findByDateAppointmentAndDoctorModelOrDateAppointmentAndPatientModel(Date dateAppoiment, DoctorModel doctorModel,
                                                                                         Date dateAppoiment2, PatientModel patientModel);

    List<AppointmentModel> findByPatientModelOrderByDateAppointment(PatientModel patientModel);
}
