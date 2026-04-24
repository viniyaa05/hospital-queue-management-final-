package com.hospital.queue.controller;

import com.hospital.queue.entity.Appointment;
import com.hospital.queue.entity.Patient;
import com.hospital.queue.entity.Token;
import com.hospital.queue.enums.CounterType;
import com.hospital.queue.enums.Gender;
import com.hospital.queue.service.AppointmentService;
import com.hospital.queue.service.DepartmentService;
import com.hospital.queue.service.PatientService;
import com.hospital.queue.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final TokenService tokenService;
    private final AppointmentService appointmentService;

    public PatientController(PatientService patientService, DepartmentService departmentService,
                             TokenService tokenService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.tokenService = tokenService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("departments", departmentService.findAllActive());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("counters", CounterType.values());
        return "patient/register";
    }

    @PostMapping("/register")
    public String registerPatient(@Valid @ModelAttribute("patient") Patient patient,
            BindingResult result, @RequestParam Long departmentId,
            @RequestParam CounterType counterType, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.findAllActive());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("counters", CounterType.values());
            return "patient/register";
        }
        Patient saved = patientService.register(patient);
        Token token = tokenService.issueToken(saved.getId(), departmentId, counterType);
        return "redirect:/patient/token/" + token.getId();
    }

    @GetMapping("/token/{id}")
    public String showToken(@PathVariable Long id, Model model) {
        Token token = tokenService.getAllTodayTokens().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst().orElse(null);
        model.addAttribute("token", token);
        return "patient/token-slip";
    }

    @GetMapping("/appointment")
    public String showAppointmentForm(Model model) {
        model.addAttribute("departments", departmentService.findAllActive());
        model.addAttribute("minDate", LocalDate.now().plusDays(1).toString());
        return "patient/appointment";
    }

    @PostMapping("/appointment")
    public String bookAppointment(@RequestParam Long patientId, @RequestParam Long departmentId,
            @RequestParam String appointmentDate, @RequestParam String appointmentTime,
            @RequestParam(required = false) String reason, Model model) {
        try {
            Patient patient = patientService.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            Appointment appt = new Appointment();
            appt.setPatient(patient);
            appt.setDepartment(departmentService.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found")));
            appt.setAppointmentDate(LocalDate.parse(appointmentDate));
            appt.setAppointmentTime(LocalTime.parse(appointmentTime));
            appt.setReason(reason);
            Appointment saved = appointmentService.book(appt);
            model.addAttribute("appointment", saved);
            return "patient/appointment-confirm";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("departments", departmentService.findAllActive());
            return "patient/appointment";
        }
    }

    @GetMapping("/search")
    public String searchPatient(@RequestParam(required = false) String phone, Model model) {
        if (phone != null && !phone.isBlank()) {
            model.addAttribute("patients", patientService.findByPhone(phone));
            model.addAttribute("phone", phone);
        }
        model.addAttribute("departments", departmentService.findAllActive());
        return "patient/search";
    }
}
