package com.example.incident.service;

import com.example.incident.dto.Dtos.*;
import com.example.incident.entity.Role;
import com.example.incident.entity.Status;
import com.example.incident.entity.User;
import com.example.incident.exception.ApiException;
import com.example.incident.repository.IncidentRepository;
import com.example.incident.repository.UserRepository;
import com.example.incident.security.CurrentUser;
import com.example.incident.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final IncidentRepository incidentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CurrentUser currentUser;

    // ---------- Profil ----------
    public ProfileResponse getProfile() {
        User user = currentUser.get();
        return new ProfileResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    // Le token contient l'email : si l'email change, on renvoie un nouveau token
    public AuthResponse updateProfile(UpdateProfileRequest request) {
        User user = currentUser.get();

        boolean emailChanged = !user.getEmail().equals(request.email());
        if (emailChanged && userRepository.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email déjà utilisé");
        }

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getFullName());
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = currentUser.get();

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Ancien mot de passe incorrect");
        }
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La confirmation ne correspond pas");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    // ---------- Administration ----------
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse setEnabled(Long id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        user.setEnabled(enabled);
        userRepository.save(user);
        return toResponse(user);
    }

    public Map<String, Long> getStatistics() {
        return Map.of(
                "totalUsers", userRepository.count(),
                "employees", userRepository.countByRole(Role.EMPLOYEE),
                "technicians", userRepository.countByRole(Role.TECHNICIAN),
                "totalIncidents", incidentRepository.count(),
                "openIncidents", incidentRepository.countByStatus(Status.OPEN),
                "inProgressIncidents", incidentRepository.countByStatus(Status.IN_PROGRESS),
                "closedIncidents", incidentRepository.countByStatus(Status.CLOSED));
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.isEnabled(), u.getCreatedAt());
    }
}
