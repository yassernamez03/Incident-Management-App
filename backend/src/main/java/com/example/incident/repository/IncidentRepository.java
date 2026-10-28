package com.example.incident.repository;

import com.example.incident.entity.Incident;
import com.example.incident.entity.Status;
import com.example.incident.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByCreatedByOrderByCreatedAtDesc(User user);
    List<Incident> findByAssignedToOrderByCreatedAtDesc(User user);
    List<Incident> findAllByOrderByCreatedAtDesc();
    long countByStatus(Status status);
}
