package com.hospital.queue.service;

import com.hospital.queue.entity.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department save(Department department);
    List<Department> findAll();
    List<Department> findAllActive();
    Optional<Department> findById(Long id);
    void deactivate(Long id);
    boolean existsByName(String name);
}
