package com.hospital.queue.config;

import com.hospital.queue.repository.AdminRepository;
import com.hospital.queue.repository.DoctorRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;

    public SecurityConfig(DoctorRepository doctorRepository, AdminRepository adminRepository) {
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Allow H2 console
                .requestMatchers("/h2-console/**").permitAll()
                // Public pages
                .requestMatchers("/", "/patient/**", "/queue/**", "/css/**", "/error", "/login").permitAll()
                .requestMatchers("/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            // Disable CSRF and frame options for H2 console
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
                .disable()
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var admin = adminRepository.findByUsername(username);
            if (admin.isPresent()) {
                return User.builder()
                        .username(admin.get().getUsername())
                        .password(admin.get().getPassword())
                        .roles("ADMIN")
                        .build();
            }
            var doctor = doctorRepository.findByUsername(username);
            if (doctor.isPresent()) {
                return User.builder()
                        .username(doctor.get().getUsername())
                        .password(doctor.get().getPassword())
                        .roles("DOCTOR")
                        .build();
            }
            throw new UsernameNotFoundException("User not found: " + username);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
