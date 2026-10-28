package com.example.incident.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class IncidentComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Incident incident;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
}
