package com.example.incident.service;

import com.example.incident.dto.Dtos.*;
import com.example.incident.entity.*;
import com.example.incident.exception.ApiException;
import com.example.incident.repository.IncidentCommentRepository;
import com.example.incident.repository.IncidentHistoryRepository;
import com.example.incident.repository.IncidentRepository;
import com.example.incident.repository.UserRepository;
import com.example.incident.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentHistoryRepository historyRepository;
    private final IncidentCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    // ---------- Création et listes ----------
    public IncidentResponse create(CreateIncidentRequest request) {
        Incident incident = new Incident();
        incident.setTitle(request.title());
        incident.setDescription(request.description());
        incident.setCategory(request.category());
        incident.setPriority(request.priority());
        incident.setStatus(Status.OPEN);
        incident.setCreatedBy(currentUser.get());
        incidentRepository.save(incident);

        addHistory(incident, "Incident créé");
        return toResponse(incident, false);
    }

    public List<IncidentResponse> getMyIncidents() {
        return incidentRepository.findByCreatedByOrderByCreatedAtDesc(currentUser.get())
                .stream().map(i -> toResponse(i, false)).toList();
    }

    public List<IncidentResponse> getAssignedIncidents() {
        return incidentRepository.findByAssignedToOrderByCreatedAtDesc(currentUser.get())
                .stream().map(i -> toResponse(i, false)).toList();
    }

    public List<IncidentResponse> getAllIncidents() {
        return incidentRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(i -> toResponse(i, false)).toList();
    }

    // ---------- Détail (avec contrôle du propriétaire) ----------
    public IncidentResponse getById(Long id) {
        Incident incident = findIncident(id);
        checkAccess(incident, currentUser.get());
        return toResponse(incident, true);
    }

    // ---------- Changement de statut ----------
    public IncidentResponse updateStatus(Long id, Status newStatus) {
        Incident incident = findIncident(id);
        User user = currentUser.get();
        checkAccess(incident, user); // un technicien ne modifie que ses incidents

        if (incident.getStatus() == Status.CLOSED && user.getRole() != Role.ADMIN) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Un incident fermé ne peut plus être modifié");
        }

        addHistory(incident, incident.getStatus() + " -> " + newStatus);
        incident.setStatus(newStatus);
        incident.setUpdatedAt(LocalDateTime.now());
        incidentRepository.save(incident);
        return toResponse(incident, true);
    }

    // ---------- Affectation (ADMIN) ----------
    public IncidentResponse assign(Long id, Long technicianId) {
        Incident incident = findIncident(id);

        User technician = userRepository.findById(technicianId)
                .filter(u -> u.getRole() == Role.TECHNICIAN)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Technicien introuvable"));

        if (incident.getStatus() == Status.CLOSED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Un incident fermé ne peut plus être modifié");
        }

        incident.setAssignedTo(technician);
        if (incident.getStatus() == Status.OPEN) {
            incident.setStatus(Status.ASSIGNED);
        }
        incident.setUpdatedAt(LocalDateTime.now());
        incidentRepository.save(incident);

        addHistory(incident, "Affecté à " + technician.getFullName());
        return toResponse(incident, true);
    }

    // ---------- Note de traitement (TECHNICIAN) ----------
    public IncidentResponse addComment(Long id, String content) {
        Incident incident = findIncident(id);
        User user = currentUser.get();
        checkAccess(incident, user);

        IncidentComment comment = new IncidentComment();
        comment.setIncident(incident);
        comment.setAuthor(user);
        comment.setContent(content);
        commentRepository.save(comment);

        return toResponse(incident, true);
    }

    // ---------- Méthodes utilitaires ----------
    private Incident findIncident(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Incident introuvable"));
    }

    // Vérifie que l'utilisateur connecté a le droit de voir / modifier cet incident
    private void checkAccess(Incident incident, User user) {
        boolean allowed = switch (user.getRole()) {
            case ADMIN -> true;
            case EMPLOYEE -> incident.getCreatedBy().getId().equals(user.getId());
            case TECHNICIAN -> incident.getAssignedTo() != null
                    && incident.getAssignedTo().getId().equals(user.getId());
        };
        if (!allowed) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Accès interdit");
        }
    }

    private void addHistory(Incident incident, String action) {
        IncidentHistory history = new IncidentHistory();
        history.setIncident(incident);
        history.setAction(action);
        historyRepository.save(history);
    }

    private IncidentResponse toResponse(Incident i, boolean withDetails) {
        List<HistoryResponse> history = null;
        List<CommentResponse> comments = null;

        if (withDetails) {
            history = historyRepository.findByIncidentOrderByDateAsc(i).stream()
                    .map(h -> new HistoryResponse(h.getDate(), h.getAction())).toList();
            comments = commentRepository.findByIncidentOrderByCreatedAtAsc(i).stream()
                    .map(c -> new CommentResponse(c.getAuthor().getFullName(), c.getContent(), c.getCreatedAt())).toList();
        }

        String assignedTo = i.getAssignedTo() == null ? null : i.getAssignedTo().getFullName();

        return new IncidentResponse(i.getId(), i.getTitle(), i.getDescription(),
                i.getCategory(), i.getPriority(), i.getStatus(),
                i.getCreatedAt(), i.getUpdatedAt(),
                i.getCreatedBy().getFullName(), assignedTo, history, comments);
    }
}
