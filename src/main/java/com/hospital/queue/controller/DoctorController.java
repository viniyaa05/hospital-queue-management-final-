package com.hospital.queue.controller;

import com.hospital.queue.entity.Doctor;
import com.hospital.queue.enums.CounterType;
import com.hospital.queue.service.AppointmentService;
import com.hospital.queue.service.DoctorService;
import com.hospital.queue.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final TokenService tokenService;
    private final AppointmentService appointmentService;

    public DoctorController(DoctorService doctorService, TokenService tokenService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.tokenService = tokenService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Doctor doctor = doctorService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Long deptId = doctor.getDepartment().getId();
        CounterType counter = CounterType.CONSULTATION;
        model.addAttribute("doctor", doctor);
        model.addAttribute("queue", tokenService.getQueue(deptId, counter));
        model.addAttribute("currentlyServing", tokenService.getCurrentlyServing(deptId, counter).orElse(null));
        model.addAttribute("todayTokens", tokenService.getTodayTokensByDepartment(deptId));
        model.addAttribute("todayAppointments", appointmentService.findByDepartmentAndDate(deptId, LocalDate.now()));
        model.addAttribute("counters", CounterType.values());
        model.addAttribute("selectedCounter", counter);
        return "doctor/dashboard";
    }

    @GetMapping("/queue")
    public String viewQueue(Authentication auth,
            @RequestParam(defaultValue = "CONSULTATION") CounterType counter, Model model) {
        Doctor doctor = doctorService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Long deptId = doctor.getDepartment().getId();
        model.addAttribute("doctor", doctor);
        model.addAttribute("queue", tokenService.getQueue(deptId, counter));
        model.addAttribute("currentlyServing", tokenService.getCurrentlyServing(deptId, counter).orElse(null));
        model.addAttribute("counters", CounterType.values());
        model.addAttribute("selectedCounter", counter);
        return "doctor/queue";
    }

    @PostMapping("/call-next")
    public String callNext(Authentication auth, @RequestParam CounterType counter, RedirectAttributes redirectAttrs) {
        Doctor doctor = doctorService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        try {
            tokenService.callNext(doctor.getDepartment().getId(), counter);
            redirectAttrs.addFlashAttribute("success", "Next patient called.");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/doctor/queue?counter=" + counter;
    }

    @PostMapping("/token/update")
    public String updateToken(@RequestParam Long tokenId, @RequestParam String status,
            @RequestParam(required = false) String notes, @RequestParam CounterType counter,
            RedirectAttributes redirectAttrs) {
        try {
            tokenService.updateStatus(tokenId, status, notes);
            redirectAttrs.addFlashAttribute("success", "Token updated: " + status);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/doctor/queue?counter=" + counter;
    }

    @PostMapping("/appointment/convert/{id}")
    public String convertAppointment(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            appointmentService.convertToToken(id);
            redirectAttrs.addFlashAttribute("success", "Appointment converted to token.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/doctor/dashboard";
    }
}
