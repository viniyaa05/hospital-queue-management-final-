package com.hospital.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hospital Queue Management System
 * Main entry point for the Spring Boot application.
 *
 * Default credentials (created on first run):
 *   Admin:  username=admin      password=admin123
 *   Doctor: username=dr.sharma  password=doctor123
 *           username=dr.priya   password=doctor123
 *
 * Run: mvn spring-boot:run
 * URL: http://localhost:8080
 */
@SpringBootApplication
public class HospitalQueueApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalQueueApplication.class, args);
    }
}
