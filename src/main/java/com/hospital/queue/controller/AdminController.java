package com.hospital.queue.controller;

import com.hospital.queue.entity.Department;
import com.hospital.queue.entity.Doctor;
import com.hospital.queue.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final TokenService tokenService;
    private final AppointmentService appointmentService;

    public AdminController(DepartmentService departmentService, DoctorService doctorService,
                           PatientService patientService, TokenService tokenService,
                           AppointmentService appointmentService) {
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.tokenService = tokenService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalDepartments", departmentService.findAllActive().size());
        model.addAttribute("totalDoctors", doctorService.findAll().size());
        model.addAttribute("todayPatients", patientService.countToday());
        model.addAttribute("todayTokens", tokenService.countTodayTokens());
        model.addAttribute("todayAppointments", appointmentService.countTodayAppointments());
        model.addAttribute("avgWaitingTime", tokenService.getAvgWaitingTimeToday());
        model.addAttribute("tokensPerDept", tokenService.getTokensPerDepartmentToday());
        return "admin/dashboard";
    }

    @GetMapping("/departments")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("newDepartment", new Department());
        return "admin/departments";
    }

    @PostMapping("/departments/add")
    public String addDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttrs) {
        if (departmentService.existsByName(department.getName())) {
            redirectAttrs.addFlashAttribute("error", "Department already exists.");
        } else {
            departmentService.save(department);
            redirectAttrs.addFlashAttribute("success", "Department added: " + department.getName());
        }
        return "redirect:/admin/departments";
    }

    @PostMapping("/departments/deactivate/{id}")
    public String deactivateDepartment(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        departmentService.deactivate(id);
        redirectAttrs.addFlashAttribute("success", "Department deactivated.");
        return "redirect:/admin/departments";
    }

    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("newDoctor", new Doctor());
        model.addAttribute("departments", departmentService.findAllActive());
        return "admin/doctors";
    }

    @PostMapping("/doctors/add")
    public String addDoctor(@ModelAttribute Doctor doctor, @RequestParam Long departmentId, RedirectAttributes redirectAttrs) {
        try {
            departmentService.findById(departmentId).ifPresent(doctor::setDepartment);
            doctorService.save(doctor);
            redirectAttrs.addFlashAttribute("success", "Doctor added: " + doctor.getName());
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/doctors";
    }

    @PostMapping("/doctors/deactivate/{id}")
    public String deactivateDoctor(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        doctorService.deactivate(id);
        redirectAttrs.addFlashAttribute("success", "Doctor deactivated.");
        return "redirect:/admin/doctors";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("todayPatients", patientService.countToday());
        model.addAttribute("todayTokens", tokenService.countTodayTokens());
        model.addAttribute("avgWaitingTime", tokenService.getAvgWaitingTimeToday());
        model.addAttribute("tokensPerDept", tokenService.getTokensPerDepartmentToday());
        model.addAttribute("todayAppointments", appointmentService.countTodayAppointments());
        model.addAttribute("allPatients", patientService.findAll());
        return "admin/reports";
    }

    @GetMapping("/queue-overview")
    public String queueOverview(Model model) {
        model.addAttribute("departments", departmentService.findAllActive());
        return "admin/queue-overview";
    }
}
