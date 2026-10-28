package com.example.incident.config;

import com.example.incident.entity.Role;
import com.example.incident.entity.User;
import com.example.incident.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Crée des comptes de test au démarrage (la base H2 est vide à chaque lancement)
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUser("Admin", "admin@test.com", "admin123", Role.ADMIN);
        createUser("Technicien A", "tech@test.com", "tech123", Role.TECHNICIAN);
        createUser("Ahmed Benali", "employee@test.com", "emp123", Role.EMPLOYEE);
    }

    private void createUser(String fullName, String email, String password, Role role) {
        if (userRepository.existsByEmail(email)) return;
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
    }
}
