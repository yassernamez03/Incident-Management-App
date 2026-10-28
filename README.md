# Gestion des incidents – Spring Boot + JWT + React

Application web pour déclarer et suivre des incidents informatiques / cybersécurité.
Trois rôles : **EMPLOYEE**, **TECHNICIAN**, **ADMIN**.

## Structure

```
incident-app/
├── backend/    Spring Boot (Java 17, Spring Security, JWT, JPA, H2)
└── frontend/   React (Vite, React Router, Axios)
```

## Lancer le backend

Prérequis : Java 17 et Maven.

```bash
cd backend
mvn spring-boot:run
```

- API : http://localhost:8080
- Console H2 : http://localhost:8080/h2-console
  (JDBC URL : `jdbc:h2:mem:incidentdb`, user : `sa`, mot de passe : vide)

La base H2 est en mémoire : elle est vidée à chaque redémarrage.

## Lancer le frontend

Prérequis : Node.js 18+.

```bash
cd frontend
npm install
npm run dev
```

- Application : http://localhost:5173

## Ports utilisés

| Service  | Port |
|----------|------|
| Backend  | 8080 |
| Frontend | 5173 |

## Comptes de test (créés au démarrage du backend)

| Rôle       | Email             | Mot de passe |
|------------|-------------------|--------------|
| ADMIN      | admin@test.com    | admin123     |
| TECHNICIAN | tech@test.com     | tech123      |
| EMPLOYEE   | employee@test.com | emp123       |

Un nouveau compte créé via la page « Créer un compte » est toujours EMPLOYEE.

## Rôles

- **EMPLOYEE** : modifie son profil, déclare des incidents, consulte uniquement ses propres incidents.
- **TECHNICIAN** : consulte les incidents qui lui sont affectés, change leur statut, ajoute des notes.
- **ADMIN** : consulte tous les incidents et utilisateurs, active/désactive un compte, affecte un incident à un technicien, consulte les statistiques.

## Principaux endpoints

| Méthode | URL | Accès |
|---------|-----|-------|
| POST | /auth/register | Public |
| POST | /auth/login | Public |
| GET / PUT | /api/profile | Connecté |
| PUT | /api/profile/password | Connecté |
| POST | /api/incidents | EMPLOYEE |
| GET | /api/incidents/my | EMPLOYEE |
| GET | /api/incidents/assigned | TECHNICIAN |
| GET | /api/incidents | ADMIN |
| GET | /api/incidents/{id} | Propriétaire / technicien affecté / ADMIN |
| PUT | /api/incidents/{id}/status | TECHNICIAN (ses incidents) ou ADMIN |
| PUT | /api/incidents/{id}/assign/{technicianId} | ADMIN |
| POST | /api/incidents/{id}/comments | TECHNICIAN (ses incidents) |
| GET | /api/admin/users | ADMIN |
| PUT | /api/admin/users/{id}/enable | ADMIN |
| PUT | /api/admin/users/{id}/disable | ADMIN |
| GET | /api/admin/statistics | ADMIN |

Les routes `/api/**` demandent le header : `Authorization: Bearer <JWT>`.

## Fonctionnalités réalisées

- Inscription (rôle EMPLOYEE forcé) et connexion avec JWT (email, rôle, dates de création / expiration)
- Mots de passe chiffrés avec BCrypt
- Token valable 5 minutes (`jwt.expiration-minutes` dans `application.properties`)
- Spring Security stateless, filtre JWT, CORS pour `http://localhost:5173`
- Contrôle des rôles avec `@PreAuthorize` et `/api/admin/**` réservé à ADMIN
- Contrôle du propriétaire d'un incident côté backend
- Compte désactivé : connexion refusée
- Incident CLOSED : modifiable uniquement par un ADMIN
- Validation des données (`@Valid`, `@NotBlank`, `@Email`, `@Size`, `@NotNull`)
- Gestion des erreurs avec `@RestControllerAdvice` (réponse `{ "status": 404, "message": "..." }`)
- Historique des incidents (`IncidentHistory`) et notes de traitement (`IncidentComment`)
- Profil : modification du nom / email et changement de mot de passe
- React : `ProtectedRoute`, `RoleRoute`, interceptors Axios (ajout du token, gestion du 401 avec message « session expirée »)
- Dashboards pour chaque rôle

Non réalisé : Refresh Token (partie optionnelle).

## Scénario de test rapide

1. Créer 2 employés via « Créer un compte ».
2. Connexion employé 1 : déclarer 2 incidents. Connexion employé 2 : déclarer 1 incident.
3. Dans Postman, avec le token de l'employé 1, appeler `GET /api/incidents/{id}` sur l'incident de l'employé 2 : réponse 403.
4. Connexion ADMIN : voir tous les incidents, ouvrir un incident et l'affecter à « Technicien A ».
5. Connexion technicien : ouvrir l'incident, passer ASSIGNED → IN_PROGRESS → RESOLVED, ajouter une note.
6. Reconnexion employé : voir le nouveau statut, l'historique et la note.
