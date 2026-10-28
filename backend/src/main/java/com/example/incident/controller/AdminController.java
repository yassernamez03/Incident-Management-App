package com.example.incident.controller;

import com.example.incident.dto.Dtos.UserResponse;
import com.example.incident.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{id}/enable")
    public UserResponse enable(@PathVariable Long id) {
        return userService.setEnabled(id, true);
    }

    @PutMapping("/users/{id}/disable")
    public UserResponse disable(@PathVariable Long id) {
        return userService.setEnabled(id, false);
    }

    @GetMapping("/statistics")
    public Map<String, Long> statistics() {
        return userService.getStatistics();
    }
}
