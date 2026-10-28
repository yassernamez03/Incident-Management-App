package com.example.incident.repository;

import com.example.incident.entity.Incident;
import com.example.incident.entity.IncidentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentCommentRepository extends JpaRepository<IncidentComment, Long> {
    List<IncidentComment> findByIncidentOrderByCreatedAtAsc(Incident incident);
}
