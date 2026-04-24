-- =============================================
-- Hospital Queue Management System
-- SQL Schema + Sample Data
-- =============================================
-- Run this BEFORE starting the application
-- or let JPA auto-create (spring.jpa.hibernate.ddl-auto=update)

CREATE DATABASE IF NOT EXISTS hospital_queue_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hospital_queue_db;

-- ── Tables ────────────────────────────────────

CREATE TABLE IF NOT EXISTS departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    location    VARCHAR(100),
    active      TINYINT(1) DEFAULT 1
);

CREATE TABLE IF NOT EXISTS admins (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(100) UNIQUE,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(30)  DEFAULT 'ROLE_ADMIN',
    active   TINYINT(1)   DEFAULT 1
);

CREATE TABLE IF NOT EXISTS doctors (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    specialization  VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    phone           VARCHAR(15),
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(30)  DEFAULT 'ROLE_DOCTOR',
    active          TINYINT(1)   DEFAULT 1,
    department_id   BIGINT       NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS patients (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    age               INT          NOT NULL,
    gender            VARCHAR(10)  NOT NULL,
    phone             VARCHAR(15)  NOT NULL,
    address           VARCHAR(255),
    emergency_contact VARCHAR(100),
    priority          VARCHAR(15)  DEFAULT 'NORMAL',
    is_emergency      TINYINT(1)   DEFAULT 0,
    registered_at     DATETIME     DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tokens (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    token_number       INT          NOT NULL,
    token_code         VARCHAR(20)  NOT NULL,
    status             VARCHAR(15)  DEFAULT 'WAITING',
    priority           VARCHAR(15)  DEFAULT 'NORMAL',
    counter_type       VARCHAR(20)  DEFAULT 'CONSULTATION',
    issue_date         DATE         NOT NULL,
    created_at         DATETIME     DEFAULT CURRENT_TIMESTAMP,
    serving_started_at DATETIME,
    completed_at       DATETIME,
    notes              TEXT,
    patient_id         BIGINT       NOT NULL,
    department_id      BIGINT       NOT NULL,
    FOREIGN KEY (patient_id)    REFERENCES patients(id),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS appointments (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_date DATE        NOT NULL,
    appointment_time TIME        NOT NULL,
    status           VARCHAR(15) DEFAULT 'WAITING',
    reason           VARCHAR(255),
    booked_at        DATETIME    DEFAULT CURRENT_TIMESTAMP,
    patient_id       BIGINT      NOT NULL,
    department_id    BIGINT      NOT NULL,
    token_id         BIGINT,
    FOREIGN KEY (patient_id)    REFERENCES patients(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (token_id)      REFERENCES tokens(id)
);

-- ── Sample Data ────────────────────────────────
-- NOTE: The DataInitializer class creates admin, departments, and sample
-- doctors automatically on startup. The SQL below is for manual reference.

-- Sample departments (auto-created by DataInitializer):
INSERT IGNORE INTO departments (name, description, location) VALUES
('Cardiology',       'Heart and cardiovascular diseases',    'Block A - Floor 1'),
('Orthopedics',      'Bone, joint and muscle conditions',    'Block B - Floor 2'),
('Neurology',        'Brain and nervous system',             'Block A - Floor 3'),
('Pediatrics',       'Children\'s health',                   'Block C - Floor 1'),
('General Medicine', 'General health consultations',         'Block D - Ground Floor'),
('Dermatology',      'Skin conditions',                      'Block B - Floor 1'),
('ENT',              'Ear, Nose and Throat',                 'Block C - Floor 2'),
('Ophthalmology',    'Eye care',                             'Block D - Floor 1');

-- Admin user (password = admin123, BCrypt encoded):
INSERT IGNORE INTO admins (name, email, username, password, role) VALUES
('System Administrator', 'admin@hospital.com', 'admin',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN');

-- Sample patients for testing:
INSERT IGNORE INTO patients (name, age, gender, phone, priority, is_emergency) VALUES
('Rajesh Kumar',    45, 'MALE',   '9876543210', 'NORMAL',    0),
('Priya Sharma',    32, 'FEMALE', '9876543211', 'NORMAL',    0),
('Murugan Pillai',  68, 'MALE',   '9876543212', 'HIGH',      0),  -- HIGH: age >= 60
('Baby Devi',        5, 'FEMALE', '9876543213', 'NORMAL',    0),
('Arjun Singh',     55, 'MALE',   '9876543214', 'EMERGENCY', 1);  -- Emergency case
