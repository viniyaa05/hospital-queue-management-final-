package com.hospital.queue.config;

import com.hospital.queue.entity.Admin;
import com.hospital.queue.entity.Department;
import com.hospital.queue.entity.Doctor;
import com.hospital.queue.enums.UserRole;
import com.hospital.queue.repository.AdminRepository;
import com.hospital.queue.repository.DepartmentRepository;
import com.hospital.queue.repository.DoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AdminRepository adminRepository,
                           DepartmentRepository departmentRepository,
                           DoctorRepository doctorRepository,
                           PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
        createDefaultDepartments();
        createSampleDoctors();
        System.out.println("✅ Data initialization complete.");
    }

    private void createDefaultAdmin() {
        if (!adminRepository.existsByUsername("admin")) {
            Admin admin = new Admin();
            admin.setName("System Administrator");
            admin.setEmail("admin@hospital.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ROLE_ADMIN);
            adminRepository.save(admin);
            System.out.println("✅ Default admin created: username=admin, password=admin123");
        }
    }

    private void createDefaultDepartments() {
        String[][] departments = {
            {"Cardiology",       "Heart and cardiovascular diseases",    "Block A - Floor 1"},
            {"Orthopedics",      "Bone, joint and muscle conditions",    "Block B - Floor 2"},
            {"Neurology",        "Brain and nervous system",             "Block A - Floor 3"},
            {"Pediatrics",       "Children's health",                    "Block C - Floor 1"},
            {"General Medicine", "General health consultations",         "Block D - Ground Floor"},
            {"Dermatology",      "Skin conditions",                      "Block B - Floor 1"},
            {"ENT",              "Ear, Nose and Throat",                 "Block C - Floor 2"},
            {"Ophthalmology",    "Eye care",                             "Block D - Floor 1"}
        };
        for (String[] dept : departments) {
            if (!departmentRepository.existsByName(dept[0])) {
                Department d = new Department();
                d.setName(dept[0]);
                d.setDescription(dept[1]);
                d.setLocation(dept[2]);
                departmentRepository.save(d);
                System.out.println("✅ Department created: " + dept[0]);
            }
        }
    }

    private void createSampleDoctors() {
        if (!doctorRepository.existsByUsername("dr.sharma")) {
            departmentRepository.findByName("Cardiology").ifPresent(cardio -> {
                Doctor doc = new Doctor();
                doc.setName("Dr. Rajesh Sharma");
                doc.setSpecialization("Cardiologist");
                doc.setEmail("rajesh.sharma@hospital.com");
                doc.setPhone("9876543210");
                doc.setUsername("dr.sharma");
                doc.setPassword(passwordEncoder.encode("doctor123"));
                doc.setDepartment(cardio);
                doctorRepository.save(doc);
                System.out.println("✅ Sample doctor created: username=dr.sharma, password=doctor123");
            });
        }
        if (!doctorRepository.existsByUsername("dr.priya")) {
            departmentRepository.findByName("General Medicine").ifPresent(general -> {
                Doctor doc = new Doctor();
                doc.setName("Dr. Priya Nair");
                doc.setSpecialization("General Physician");
                doc.setEmail("priya.nair@hospital.com");
                doc.setPhone("9876543211");
                doc.setUsername("dr.priya");
                doc.setPassword(passwordEncoder.encode("doctor123"));
                doc.setDepartment(general);
                doctorRepository.save(doc);
                System.out.println("✅ Sample doctor created: username=dr.priya, password=doctor123");
            });
        }
    }
}
