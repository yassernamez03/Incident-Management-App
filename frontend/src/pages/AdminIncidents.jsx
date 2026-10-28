import { useEffect, useState } from "react";
import { getAllIncidents } from "../services/incidentService";
import { getErrorMessage } from "../services/api";
import IncidentTable from "../components/IncidentTable";

export default function AdminIncidents() {
  const [incidents, setIncidents] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    getAllIncidents()
      .then((res) => setIncidents(res.data))
      .catch((err) => setError(getErrorMessage(err)));
  }, []);

  return (
    <div>
      <h2>Tous les incidents</h2>
      <p>Ouvrez un incident (Détails) pour l'affecter à un technicien.</p>
      {error && <p className="error">{error}</p>}
      <IncidentTable incidents={incidents} />
    </div>
  );
}
