import { useEffect, useState } from "react";
import { getAssignedIncidents } from "../services/incidentService";
import { getErrorMessage } from "../services/api";
import IncidentTable from "../components/IncidentTable";

export default function AssignedIncidents() {
  const [incidents, setIncidents] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    getAssignedIncidents()
      .then((res) => setIncidents(res.data))
      .catch((err) => setError(getErrorMessage(err)));
  }, []);

  return (
    <div>
      <h2>Incidents qui me sont affectés</h2>
      {error && <p className="error">{error}</p>}
      <IncidentTable incidents={incidents} />
    </div>
  );
}
