package com.hospital.queue.serviceImpl;

import com.hospital.queue.entity.Doctor;
import com.hospital.queue.repository.DoctorRepository;
import com.hospital.queue.service.DoctorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override public Doctor save(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }
    @Override @Transactional(readOnly=true) public Optional<Doctor> findById(Long id) { return doctorRepository.findById(id); }
    @Override @Transactional(readOnly=true) public Optional<Doctor> findByUsername(String username) { return doctorRepository.findByUsername(username); }
    @Override @Transactional(readOnly=true) public List<Doctor> findAll() { return doctorRepository.findAll(); }
    @Override @Transactional(readOnly=true) public List<Doctor> findByDepartment(Long departmentId) { return doctorRepository.findByDepartmentId(departmentId); }
    @Override public void deactivate(Long id) {
        doctorRepository.findById(id).ifPresent(doc -> { doc.setActive(false); doctorRepository.save(doc); });
    }
}
