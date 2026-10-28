package com.example.incident.controller;

import com.example.incident.dto.Dtos.*;
import com.example.incident.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public IncidentResponse create(@Valid @RequestBody CreateIncidentRequest request) {
        return incidentService.create(request);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<IncidentResponse> myIncidents() {
        return incidentService.getMyIncidents();
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public List<IncidentResponse> assignedIncidents() {
        return incidentService.getAssignedIncidents();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<IncidentResponse> allIncidents() {
        return incidentService.getAllIncidents();
    }

    // Tous les rôles : le service vérifie que l'incident appartient bien à l'utilisateur
    @GetMapping("/{id}")
    public IncidentResponse getById(@PathVariable Long id) {
        return incidentService.getById(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN')")
    public IncidentResponse updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return incidentService.updateStatus(id, request.status());
    }

    @PutMapping("/{id}/assign/{technicianId}")
    @PreAuthorize("hasRole('ADMIN')")
    public IncidentResponse assign(@PathVariable Long id, @PathVariable Long technicianId) {
        return incidentService.assign(id, technicianId);
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public IncidentResponse addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        return incidentService.addComment(id, request.content());
    }
}
