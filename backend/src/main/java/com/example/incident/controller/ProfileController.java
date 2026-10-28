package com.example.incident.controller;

import com.example.incident.dto.Dtos.*;
import com.example.incident.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ProfileResponse getProfile() {
        return userService.getProfile();
    }

    @PutMapping
    public AuthResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(request);
    }

    @PutMapping("/password")
    public Map<String, String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return Map.of("message", "Mot de passe modifié");
    }
}
