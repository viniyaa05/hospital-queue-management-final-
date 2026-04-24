package com.hospital.queue.service;

import com.hospital.queue.entity.Token;
import com.hospital.queue.enums.CounterType;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TokenService {
    Token issueToken(Long patientId, Long departmentId, CounterType counterType);
    List<Token> getQueue(Long departmentId, CounterType counterType);
    Optional<Token> getCurrentlyServing(Long departmentId, CounterType counterType);
    Token callNext(Long departmentId, CounterType counterType);
    Token updateStatus(Long tokenId, String status, String notes);
    List<Token> getTodayTokensByDepartment(Long departmentId);
    Double getAvgWaitingTimeToday();
    Map<String, Long> getTokensPerDepartmentToday();
    long countTodayTokens();
    List<Token> getAllTodayTokens();
}
