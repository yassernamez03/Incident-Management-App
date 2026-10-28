package com.example.incident.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// Une seule exception pour toutes les erreurs métier : un statut HTTP + un message
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
