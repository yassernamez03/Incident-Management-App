import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyIncidents } from "../services/incidentService";
import { getErrorMessage } from "../services/api";
import IncidentTable from "../components/IncidentTable";

export default function MyIncidents() {
  const [incidents, setIncidents] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    getMyIncidents()
      .then((res) => setIncidents(res.data))
      .catch((err) => setError(getErrorMessage(err)));
  }, []);

  return (
    <div>
      <h2>Mes incidents</h2>
      <p><Link to="/new-incident">+ Déclarer un incident</Link></p>
      {error && <p className="error">{error}</p>}
      <IncidentTable incidents={incidents} />
    </div>
  );
}
