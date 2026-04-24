package com.hospital.queue.entity;

import com.hospital.queue.enums.CounterType;
import com.hospital.queue.enums.Priority;
import com.hospital.queue.enums.TokenStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_number", nullable = false)
    private int tokenNumber;

    @Column(name = "token_code", nullable = false)
    private String tokenCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TokenStatus status = TokenStatus.WAITING;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "counter_type", nullable = false)
    private CounterType counterType = CounterType.CONSULTATION;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "serving_started_at")
    private LocalDateTime servingStartedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.issueDate = LocalDate.now();
    }

    public long getWaitingMinutes() {
        if (servingStartedAt != null && createdAt != null) {
            return java.time.Duration.between(createdAt, servingStartedAt).toMinutes();
        }
        return -1;
    }

    public Token() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(int tokenNumber) { this.tokenNumber = tokenNumber; }
    public String getTokenCode() { return tokenCode; }
    public void setTokenCode(String tokenCode) { this.tokenCode = tokenCode; }
    public TokenStatus getStatus() { return status; }
    public void setStatus(TokenStatus status) { this.status = status; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public CounterType getCounterType() { return counterType; }
    public void setCounterType(CounterType counterType) { this.counterType = counterType; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getServingStartedAt() { return servingStartedAt; }
    public void setServingStartedAt(LocalDateTime servingStartedAt) { this.servingStartedAt = servingStartedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
