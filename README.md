# 🏥 Hospital Queue Management System
### Java Spring Boot · No JavaScript · Thymeleaf · MySQL · JPA

---

## ✅ Features Implemented

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

## 🔧 Prerequisites

- **Java 17+** (`java -version`)
- **Maven 3.8+** (`mvn -version`)
- **MySQL 8.0+** running on localhost:3306

---

## 🚀 Setup & Run (Step by Step)

### Step 1 — Create MySQL Database

```sql
CREATE DATABASE hospital_queue_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Or run the full schema file:
```bash
mysql -u root -p < schema.sql
```

### Step 2 — Configure Database Password

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_queue_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

### Step 3 — Build & Run

```bash
cd hospital-queue-system
mvn clean install
mvn spring-boot:run
```

### Step 4 — Open in Browser

```
http://localhost:8080
```

---

## 🔑 Default Login Credentials

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
            ├── static/css/style.css              ← All CSS (no JS)
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

## 🌐 URL Reference

| URL | Access | Description |
|---|---|---|
| `/` | Public | Landing page |
| `/login` | Public | Staff login |
| `/patient/register` | Public | Register patient & get token |
| `/patient/appointment` | Public | Book appointment |
| `/patient/search` | Public | Search patient by phone |
| `/queue/display` | Public | Live queue board (auto-refresh) |
| `/queue/department/{id}` | Public | Department-specific queue |
| `/doctor/dashboard` | Doctor | Doctor's queue dashboard |
| `/doctor/queue` | Doctor | Multi-counter queue view |
| `/admin/dashboard` | Admin | Admin overview |
| `/admin/departments` | Admin | Manage departments |
| `/admin/doctors` | Admin | Manage doctors |
| `/admin/reports` | Admin | Analytics & reports |

---

## 🔄 Priority Queue Logic

Patients are queued in this order:
1. 🚨 **EMERGENCY** — checked in as emergency (override by staff)
2. ⭐ **HIGH** — auto-assigned to patients aged 60+
3. ✅ **NORMAL** — all other walk-in patients

Within each priority, ordering is **FIFO** (first come, first served by creation time).

---

## 📊 Multi-Counter Support

Each department has 4 independent queues:
- **Consultation** — doctor visits
- **Billing** — payment counter
- **Lab** — laboratory tests
- **Pharmacy** — medicine collection

Each counter issues its own sequential token numbers.

---

## ⚠️ Notes

- `spring.jpa.hibernate.ddl-auto=update` — tables are auto-created/updated
- Change to `validate` in production after initial setup
- For production: enable CSRF, use HTTPS, set strong passwords
- Queue display uses HTML `<meta http-equiv="refresh">` — no JavaScript needed
- Passwords are BCrypt-encoded (strength 10)
