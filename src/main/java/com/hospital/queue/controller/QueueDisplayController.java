package com.hospital.queue.controller;

import com.hospital.queue.enums.CounterType;
import com.hospital.queue.service.DepartmentService;
import com.hospital.queue.service.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/queue")
public class QueueDisplayController {

    private final TokenService tokenService;
    private final DepartmentService departmentService;

    public QueueDisplayController(TokenService tokenService, DepartmentService departmentService) {
        this.tokenService = tokenService;
        this.departmentService = departmentService;
    }

    @GetMapping("/display")
    public String displayQueue(Model model) {
        model.addAttribute("departments", departmentService.findAllActive());
        model.addAttribute("counters", CounterType.values());
        model.addAttribute("refreshSeconds", 15);
        return "queue/display";
    }

    @GetMapping("/department/{deptId}")
    public String departmentQueue(@PathVariable Long deptId,
            @RequestParam(defaultValue = "CONSULTATION") CounterType counter, Model model) {
        model.addAttribute("department", departmentService.findById(deptId).orElse(null));
        model.addAttribute("queue", tokenService.getQueue(deptId, counter));
        model.addAttribute("currentlyServing", tokenService.getCurrentlyServing(deptId, counter).orElse(null));
        model.addAttribute("selectedCounter", counter);
        model.addAttribute("counters", CounterType.values());
        model.addAttribute("refreshSeconds", 10);
        return "queue/department-queue";
    }
}
