package com.hospital.queue.serviceImpl;

import com.hospital.queue.entity.Appointment;
import com.hospital.queue.entity.Token;
import com.hospital.queue.enums.CounterType;
import com.hospital.queue.enums.TokenStatus;
import com.hospital.queue.repository.AppointmentRepository;
import com.hospital.queue.service.AppointmentService;
import com.hospital.queue.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Override public Appointment book(Appointment appointment) { return appointmentRepository.save(appointment); }
    @Override @Transactional(readOnly=true) public Optional<Appointment> findById(Long id) { return appointmentRepository.findById(id); }
    @Override @Transactional(readOnly=true) public List<Appointment> findByPatient(Long patientId) { return appointmentRepository.findByPatientId(patientId); }
    @Override @Transactional(readOnly=true) public List<Appointment> findByDepartmentAndDate(Long departmentId, LocalDate date) { return appointmentRepository.findByDepartmentIdAndAppointmentDate(departmentId, date); }
    @Override @Transactional(readOnly=true) public List<Appointment> findTodayAppointments() { return appointmentRepository.findByAppointmentDate(LocalDate.now()); }
    @Override @Transactional(readOnly=true) public long countTodayAppointments() { return appointmentRepository.countByAppointmentDate(LocalDate.now()); }

    @Override
    public void convertToToken(Long appointmentId) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        Token token = tokenService.issueToken(
                appt.getPatient().getId(),
                appt.getDepartment().getId(),
                CounterType.CONSULTATION);
        appt.setStatus(TokenStatus.SERVING);
        appt.setGeneratedToken(token);
        appointmentRepository.save(appt);
    }
}
