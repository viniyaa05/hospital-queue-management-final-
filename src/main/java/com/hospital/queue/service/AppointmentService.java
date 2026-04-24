package com.hospital.queue.service;

import com.hospital.queue.entity.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment book(Appointment appointment);
    Optional<Appointment> findById(Long id);
    List<Appointment> findByPatient(Long patientId);
    List<Appointment> findByDepartmentAndDate(Long departmentId, LocalDate date);
    List<Appointment> findTodayAppointments();
    // Converts appointment into a live token
    void convertToToken(Long appointmentId);
    long countTodayAppointments();
}
