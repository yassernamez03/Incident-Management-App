package com.example.incident.dto;

import com.example.incident.entity.Category;
import com.example.incident.entity.Priority;
import com.example.incident.entity.Role;
import com.example.incident.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

// Tous les objets d'entrée / sortie de l'API sont regroupés ici.
public class Dtos {

    // ---------- Authentification ----------
    public record RegisterRequest(
            @NotBlank(message = "Le nom est obligatoire") String fullName,
            @NotBlank(message = "L'email est obligatoire") @Email(message = "Email invalide") String email,
            @NotBlank(message = "Le mot de passe est obligatoire")
            @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères") String password) {}

    public record LoginRequest(
            @NotBlank(message = "L'email est obligatoire") String email,
            @NotBlank(message = "Le mot de passe est obligatoire") String password) {}

    public record AuthResponse(String token, String role, String fullName) {}

    // ---------- Profil ----------
    public record ProfileResponse(Long id, String fullName, String email, String role) {}

    public record UpdateProfileRequest(
            @NotBlank(message = "Le nom est obligatoire") String fullName,
            @NotBlank(message = "L'email est obligatoire") @Email(message = "Email invalide") String email) {}

    public record ChangePasswordRequest(
            @NotBlank(message = "L'ancien mot de passe est obligatoire") String oldPassword,
            @NotBlank(message = "Le nouveau mot de passe est obligatoire")
            @Size(min = 6, message = "Le nouveau mot de passe doit contenir au moins 6 caractères") String newPassword,
            @NotBlank(message = "La confirmation est obligatoire") String confirmPassword) {}

    // ---------- Admin ----------
    public record UserResponse(Long id, String fullName, String email, Role role,
                               boolean enabled, LocalDateTime createdAt) {}

    // ---------- Incidents ----------
    public record CreateIncidentRequest(
            @NotBlank(message = "Le titre est obligatoire") String title,
            @NotBlank(message = "La description est obligatoire") String description,
            @NotNull(message = "La catégorie est obligatoire") Category category,
            @NotNull(message = "La priorité est obligatoire") Priority priority) {}

    public record StatusRequest(@NotNull(message = "Le statut est obligatoire") Status status) {}

    public record CommentRequest(@NotBlank(message = "La note est obligatoire") String content) {}

    public record HistoryResponse(LocalDateTime date, String action) {}

    public record CommentResponse(String author, String content, LocalDateTime createdAt) {}

    // history et comments sont remplis seulement pour le détail d'un incident
    public record IncidentResponse(Long id, String title, String description,
                                   Category category, Priority priority, Status status,
                                   LocalDateTime createdAt, LocalDateTime updatedAt,
                                   String createdBy, String assignedTo,
                                   List<HistoryResponse> history, List<CommentResponse> comments) {}
}
