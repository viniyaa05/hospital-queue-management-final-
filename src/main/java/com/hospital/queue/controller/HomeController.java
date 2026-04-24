package com.hospital.queue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles home and login routes.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Landing page
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        // Spring Security will redirect to correct dashboard based on role
        return "redirect:/";
    }
}
