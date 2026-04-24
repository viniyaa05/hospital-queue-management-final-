package com.hospital.queue.repository;

import com.hospital.queue.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Find all active departments
    List<Department> findByActiveTrue();

    // Find by name
    Optional<Department> findByName(String name);

    // Check if name exists
    boolean existsByName(String name);
}
