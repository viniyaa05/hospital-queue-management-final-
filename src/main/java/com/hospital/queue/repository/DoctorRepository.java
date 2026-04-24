package com.hospital.queue.repository;

import com.hospital.queue.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Doctor entity.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUsername(String username);

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findByDepartmentId(Long departmentId);

    List<Doctor> findByActiveTrue();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
