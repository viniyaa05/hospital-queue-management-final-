package com.hospital.queue.serviceImpl;

import com.hospital.queue.entity.Department;
import com.hospital.queue.repository.DepartmentRepository;
import com.hospital.queue.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override public Department save(Department department) { return departmentRepository.save(department); }
    @Override @Transactional(readOnly=true) public List<Department> findAll() { return departmentRepository.findAll(); }
    @Override @Transactional(readOnly=true) public List<Department> findAllActive() { return departmentRepository.findByActiveTrue(); }
    @Override @Transactional(readOnly=true) public Optional<Department> findById(Long id) { return departmentRepository.findById(id); }
    @Override public void deactivate(Long id) {
        departmentRepository.findById(id).ifPresent(dept -> { dept.setActive(false); departmentRepository.save(dept); });
    }
    @Override @Transactional(readOnly=true) public boolean existsByName(String name) { return departmentRepository.existsByName(name); }
}
