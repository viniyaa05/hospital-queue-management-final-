package com.hospital.queue.serviceImpl;

import com.hospital.queue.entity.Patient;
import com.hospital.queue.repository.PatientRepository;
import com.hospital.queue.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override public Patient register(Patient patient) { return patientRepository.save(patient); }
    @Override @Transactional(readOnly=true) public Optional<Patient> findById(Long id) { return patientRepository.findById(id); }
    @Override @Transactional(readOnly=true) public List<Patient> findAll() { return patientRepository.findAll(); }
    @Override @Transactional(readOnly=true) public List<Patient> findByPhone(String phone) { return patientRepository.findByPhone(phone); }
    @Override @Transactional(readOnly=true) public long countToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        return patientRepository.countPatientsToday(start, end);
    }
}
