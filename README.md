# Hospital Queue Management System
### Java Spring Boot · No JavaScript · Thymeleaf · MySQL · JPA

---

## Features Implemented

| Feature | Details |
|---|---|
| Patient Registration | Name, Age, Gender, Phone, Emergency flag |
| Priority Queue | EMERGENCY → HIGH (age≥60) → NORMAL |
| Multi-Counter Support | Consultation · Billing · Lab · Pharmacy |
| Department Queues | Separate queue per department+counter |
| Doctor Dashboard | View queue, Call Next, Complete/Skip |
| Admin Panel | Manage Doctors & Departments |
| Appointment System | Book future appointments, convert to token |
| Reports & Analytics | Daily patients, avg wait time, tokens/dept |
| Queue Display Board | Public screen with meta-refresh (No JS!) |
| Spring Security | BCrypt login for Doctor & Admin roles |
| No JavaScript | 100% server-side rendering via Thymeleaf |

---

---
Default Login Credentials

| Role   | Username    | Password    |
|--------|-------------|-------------|
| Admin  | `admin`     | `admin123`  |
| Doctor | `dr.sharma` | `doctor123` |
| Doctor | `dr.priya`  | `doctor123` |

> Credentials are created automatically on first startup by `DataInitializer.java`

---

## 📁 Project Structure

```
hospital-queue-system/
├── pom.xml
├── schema.sql
├── README.md
└── src/
    └── main/
        ├── java/com/hospital/queue/
        │   ├── HospitalQueueApplication.java     ← Main class
        │   ├── config/
        │   │   ├── SecurityConfig.java           ← Spring Security
        │   │   └── DataInitializer.java          ← Seed data
        │   ├── enums/
        │   │   ├── TokenStatus.java              ← WAITING/SERVING/COMPLETED/SKIPPED
        │   │   ├── Priority.java                 ← EMERGENCY/HIGH/NORMAL
        │   │   ├── CounterType.java              ← CONSULTATION/BILLING/LAB/PHARMACY
        │   │   ├── Gender.java
        │   │   └── UserRole.java
        │   ├── entity/
        │   │   ├── Patient.java
        │   │   ├── Token.java                    ← Core queue entity
        │   │   ├── Department.java
        │   │   ├── Doctor.java
        │   │   ├── Admin.java
        │   │   └── Appointment.java
        │   ├── repository/
        │   │   ├── PatientRepository.java
        │   │   ├── TokenRepository.java          ← Complex queue queries
        │   │   ├── DepartmentRepository.java
        │   │   ├── DoctorRepository.java
        │   │   ├── AdminRepository.java
        │   │   └── AppointmentRepository.java
        │   ├── service/
        │   │   ├── PatientService.java
        │   │   ├── TokenService.java
        │   │   ├── DepartmentService.java
        │   │   ├── DoctorService.java
        │   │   └── AppointmentService.java
        │   ├── serviceImpl/
        │   │   ├── PatientServiceImpl.java
        │   │   ├── TokenServiceImpl.java         ← Priority queue logic
        │   │   ├── DepartmentServiceImpl.java
        │   │   ├── DoctorServiceImpl.java
        │   │   └── AppointmentServiceImpl.java
        │   └── controller/
        │       ├── HomeController.java
        │       ├── PatientController.java
        │       ├── QueueDisplayController.java
        │       ├── DoctorController.java
        │       └── AdminController.java
        └── resources/
            ├── application.properties
            ├── static/css/style.css              
            └── templates/
                ├── fragments.html                ← Navbar, sidebar, footer
                ├── index.html                    ← Landing page
                ├── login.html
                ├── patient/
                │   ├── register.html
                │   ├── token-slip.html
                │   ├── appointment.html
                │   ├── appointment-confirm.html
                │   └── search.html
                ├── queue/
                │   ├── display.html              ← Public board (meta-refresh)
                │   └── department-queue.html
                ├── doctor/
                │   ├── dashboard.html
                │   └── queue.html
                └── admin/
                    ├── dashboard.html
                    ├── departments.html
                    ├── doctors.html
                    ├── reports.html
                    └── queue-overview.html
```

---

---




---

