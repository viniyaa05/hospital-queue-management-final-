package com.hospital.queue.service;

import com.hospital.queue.entity.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient register(Patient patient);
    Optional<Patient> findById(Long id);
    List<Patient> findAll();
    List<Patient> findByPhone(String phone);
    long countToday();
}
