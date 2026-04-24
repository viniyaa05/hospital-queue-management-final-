package com.hospital.queue.serviceImpl;

import com.hospital.queue.entity.Department;
import com.hospital.queue.entity.Patient;
import com.hospital.queue.entity.Token;
import com.hospital.queue.enums.CounterType;
import com.hospital.queue.enums.TokenStatus;
import com.hospital.queue.repository.DepartmentRepository;
import com.hospital.queue.repository.PatientRepository;
import com.hospital.queue.repository.TokenRepository;
import com.hospital.queue.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;

    public TokenServiceImpl(TokenRepository tokenRepository,
                            PatientRepository patientRepository,
                            DepartmentRepository departmentRepository) {
        this.tokenRepository = tokenRepository;
        this.patientRepository = patientRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Token issueToken(Long patientId, Long departmentId, CounterType counterType) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));

        int nextNum = tokenRepository.findMaxTokenNumber(departmentId, counterType, LocalDate.now()) + 1;
        String prefix = department.getName().substring(0, Math.min(4, department.getName().length())).toUpperCase();
        String tokenCode = prefix + "-" + String.format("%03d", nextNum);

        Token token = new Token();
        token.setTokenNumber(nextNum);
        token.setTokenCode(tokenCode);
        token.setPatient(patient);
        token.setDepartment(department);
        token.setCounterType(counterType);
        token.setPriority(patient.getPriority());
        token.setStatus(TokenStatus.WAITING);
        return tokenRepository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Token> getQueue(Long departmentId, CounterType counterType) {
        return tokenRepository.findQueueOrdered(departmentId, counterType, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Token> getCurrentlyServing(Long departmentId, CounterType counterType) {
        return tokenRepository.findByDepartmentIdAndCounterTypeAndStatus(
                departmentId, counterType, TokenStatus.SERVING);
    }

    @Override
    public Token callNext(Long departmentId, CounterType counterType) {
        tokenRepository.findByDepartmentIdAndCounterTypeAndStatus(
                departmentId, counterType, TokenStatus.SERVING)
                .ifPresent(current -> {
                    current.setStatus(TokenStatus.COMPLETED);
                    current.setCompletedAt(LocalDateTime.now());
                    tokenRepository.save(current);
                });

        List<Token> queue = tokenRepository.findQueueOrdered(departmentId, counterType, LocalDate.now());
        if (queue.isEmpty()) throw new RuntimeException("No patients waiting in queue");

        Token next = queue.get(0);
        next.setStatus(TokenStatus.SERVING);
        next.setServingStartedAt(LocalDateTime.now());
        return tokenRepository.save(next);
    }

    @Override
    public Token updateStatus(Long tokenId, String status, String notes) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found: " + tokenId));
        token.setStatus(TokenStatus.valueOf(status.toUpperCase()));
        token.setNotes(notes);
        if (status.equalsIgnoreCase("COMPLETED")) {
            token.setCompletedAt(LocalDateTime.now());
        }
        return tokenRepository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Token> getTodayTokensByDepartment(Long departmentId) {
        return tokenRepository.findByDepartmentAndDate(departmentId, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAvgWaitingTimeToday() {
        // H2-compatible: calculate in Java instead of SQL TIMESTAMPDIFF
        List<Token> completed = tokenRepository.findCompletedTokensForDate(LocalDate.now());
        if (completed.isEmpty()) return 0.0;
        double totalMinutes = completed.stream()
                .filter(t -> t.getServingStartedAt() != null && t.getCreatedAt() != null)
                .mapToLong(t -> Duration.between(t.getCreatedAt(), t.getServingStartedAt()).toMinutes())
                .average()
                .orElse(0.0);
        return totalMinutes;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getTokensPerDepartmentToday() {
        List<Object[]> results = tokenRepository.countTokensPerDepartmentForDate(LocalDate.now());
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public long countTodayTokens() {
        return tokenRepository.findByIssueDate(LocalDate.now()).size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Token> getAllTodayTokens() {
        return tokenRepository.findByIssueDate(LocalDate.now());
    }
}
