package com.example.incident.repository;

import com.example.incident.entity.Incident;
import com.example.incident.entity.IncidentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentHistoryRepository extends JpaRepository<IncidentHistory, Long> {
    List<IncidentHistory> findByIncidentOrderByDateAsc(Incident incident);
}
