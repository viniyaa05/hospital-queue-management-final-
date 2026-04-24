package com.hospital.queue.repository;

import com.hospital.queue.entity.Token;
import com.hospital.queue.enums.CounterType;
import com.hospital.queue.enums.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT COALESCE(MAX(t.tokenNumber), 0) FROM Token t " +
           "WHERE t.department.id = :deptId AND t.counterType = :counter AND t.issueDate = :date")
    int findMaxTokenNumber(@Param("deptId") Long deptId,
                           @Param("counter") CounterType counter,
                           @Param("date") LocalDate date);

    @Query("SELECT t FROM Token t WHERE t.department.id = :deptId " +
           "AND t.counterType = :counter AND t.status = 'WAITING' AND t.issueDate = :date " +
           "ORDER BY CASE t.priority WHEN 'EMERGENCY' THEN 1 WHEN 'HIGH' THEN 2 ELSE 3 END ASC, t.createdAt ASC")
    List<Token> findQueueOrdered(@Param("deptId") Long deptId,
                                 @Param("counter") CounterType counter,
                                 @Param("date") LocalDate date);

    Optional<Token> findByDepartmentIdAndCounterTypeAndStatus(
            Long departmentId, CounterType counterType, TokenStatus status);

    @Query("SELECT t FROM Token t WHERE t.department.id = :deptId AND t.issueDate = :date " +
           "ORDER BY CASE t.priority WHEN 'EMERGENCY' THEN 1 WHEN 'HIGH' THEN 2 ELSE 3 END ASC, t.createdAt ASC")
    List<Token> findByDepartmentAndDate(@Param("deptId") Long deptId,
                                        @Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Token t WHERE t.department.id = :deptId " +
           "AND t.status = 'WAITING' AND t.issueDate = :date")
    long countWaitingByDepartment(@Param("deptId") Long deptId,
                                  @Param("date") LocalDate date);

    List<Token> findByIssueDate(LocalDate date);

    @Query("SELECT t.department.name, COUNT(t) FROM Token t " +
           "WHERE t.issueDate = :date GROUP BY t.department.name")
    List<Object[]> countTokensPerDepartmentForDate(@Param("date") LocalDate date);

    // H2-compatible average waiting time (uses Java duration, not SQL function)
    @Query("SELECT t FROM Token t WHERE t.status = 'COMPLETED' " +
           "AND t.issueDate = :date AND t.servingStartedAt IS NOT NULL")
    List<Token> findCompletedTokensForDate(@Param("date") LocalDate date);

    List<Token> findByDepartmentIdAndCounterTypeAndIssueDate(
            Long departmentId, CounterType counterType, LocalDate issueDate);
}
