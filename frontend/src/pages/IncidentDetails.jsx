import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getRole } from "../services/authService";
import { getErrorMessage } from "../services/api";
import { getIncident, updateStatus, assignIncident, addComment, getUsers } from "../services/incidentService";

export default function IncidentDetails() {
  const { id } = useParams();
  const role = getRole();

  const [incident, setIncident] = useState(null);
  const [technicians, setTechnicians] = useState([]);
  const [status, setStatus] = useState("IN_PROGRESS");
  const [technicianId, setTechnicianId] = useState("");
  const [note, setNote] = useState("");
  const [error, setError] = useState("");

  function loadIncident() {
    getIncident(id)
      .then((res) => setIncident(res.data))
      .catch((err) => setError(getErrorMessage(err)));
  }

  useEffect(() => {
    loadIncident();
    if (role === "ADMIN") {
      getUsers().then((res) => setTechnicians(res.data.filter((u) => u.role === "TECHNICIAN")));
    }
  }, [id]);

  // Exécute une action puis recharge l'incident
  async function run(action) {
    setError("");
    try {
      await action();
      setNote("");
      loadIncident();
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  if (!incident) return <p className="error">{error || "Chargement..."}</p>;

  return (
    <div>
      <h2>{incident.title}</h2>
      {error && <p className="error">{error}</p>}

      <div className="box">
        <p>{incident.description}</p>
        <p>
          Catégorie : <b>{incident.category}</b> | Priorité : <b>{incident.priority}</b> | Statut : <b>{incident.status}</b>
        </p>
        <p>Créé par : {incident.createdBy} | Affecté à : {incident.assignedTo || "-"}</p>
      </div>

      {/* ADMIN : affecter à un technicien */}
      {role === "ADMIN" && (
        <div className="box">
          <b>Affecter à un technicien : </b>
          <select value={technicianId} onChange={(e) => setTechnicianId(e.target.value)}>
            <option value="">-- choisir --</option>
            {technicians.map((t) => (
              <option key={t.id} value={t.id}>{t.fullName}</option>
            ))}
          </select>{" "}
          <button disabled={!technicianId} onClick={() => run(() => assignIncident(id, technicianId))}>Affecter</button>
        </div>
      )}

      {/* TECHNICIAN ou ADMIN : changer le statut */}
      {(role === "TECHNICIAN" || role === "ADMIN") && (
        <div className="box">
          <b>Changer le statut : </b>
          <select value={status} onChange={(e) => setStatus(e.target.value)}>
            {["OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"].map((s) => (
              <option key={s}>{s}</option>
            ))}
          </select>{" "}
          <button onClick={() => run(() => updateStatus(id, status))}>Modifier</button>
        </div>
      )}

      <h3>Historique</h3>
      <div className="box">
        {incident.history.map((h, index) => (
          <div key={index}>{new Date(h.date).toLocaleString()} : {h.action}</div>
        ))}
      </div>

      <h3>Notes de traitement</h3>
      <div className="box">
        {incident.comments.length === 0 && <p>Aucune note.</p>}
        {incident.comments.map((c, index) => (
          <p key={index}>
            <b>{c.author}</b> ({new Date(c.createdAt).toLocaleString()}) : {c.content}
          </p>
        ))}
      </div>

      {/* TECHNICIAN : ajouter une note */}
      {role === "TECHNICIAN" && (
        <form onSubmit={(e) => { e.preventDefault(); run(() => addComment(id, note)); }}>
          <textarea placeholder="Ajouter une note de traitement" rows="3" value={note} onChange={(e) => setNote(e.target.value)} />
          <button type="submit">Ajouter la note</button>
        </form>
      )}
    </div>
  );
}
