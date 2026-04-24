package com.hospital.queue.repository;

import com.hospital.queue.entity.Appointment;
import com.hospital.queue.enums.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDepartmentIdAndAppointmentDate(Long departmentId, LocalDate date);

    List<Appointment> findByAppointmentDateAndStatus(LocalDate date, TokenStatus status);

    List<Appointment> findByAppointmentDate(LocalDate date);

    long countByAppointmentDate(LocalDate date);
}
