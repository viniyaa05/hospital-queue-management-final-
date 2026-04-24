package com.hospital.queue.repository;

import com.hospital.queue.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Patient entity.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find by phone number
    List<Patient> findByPhone(String phone);

    // Find patients registered today
    @Query("SELECT p FROM Patient p WHERE p.registeredAt >= :startOfDay AND p.registeredAt <= :endOfDay")
    List<Patient> findPatientsRegisteredToday(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    // Count patients registered today
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.registeredAt >= :startOfDay AND p.registeredAt <= :endOfDay")
    long countPatientsToday(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    // Find emergency patients
    List<Patient> findByEmergencyTrue();
}
