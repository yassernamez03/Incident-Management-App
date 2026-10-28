package com.example.incident.security;

import com.example.incident.entity.User;
import com.example.incident.exception.ApiException;
import com.example.incident.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// Retourne l'utilisateur actuellement connecté (celui du token JWT)
@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final UserRepository userRepository;

    public User get() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }
}
