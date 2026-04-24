package com.hospital.queue.service;

import com.hospital.queue.entity.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor save(Doctor doctor);
    Optional<Doctor> findById(Long id);
    Optional<Doctor> findByUsername(String username);
    List<Doctor> findAll();
    List<Doctor> findByDepartment(Long departmentId);
    void deactivate(Long id);
}
