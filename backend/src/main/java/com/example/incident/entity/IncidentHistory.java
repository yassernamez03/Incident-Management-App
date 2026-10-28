package com.example.incident.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class IncidentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Incident incident;

    // Exemple : "Incident créé", "OPEN -> IN_PROGRESS"
    private String action;

    private LocalDateTime date = LocalDateTime.now();
}
