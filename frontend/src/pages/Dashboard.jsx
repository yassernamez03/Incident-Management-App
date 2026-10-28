import { useEffect, useState } from "react";
import { getRole, getFullName } from "../services/authService";
import { getMyIncidents, getAssignedIncidents, getStatistics } from "../services/incidentService";
import IncidentTable from "../components/IncidentTable";

function Stat({ label, value }) {
  return (
    <div className="card">
      <b>{value}</b>
      {label}
    </div>
  );
}

function EmployeeDashboard() {
  const [incidents, setIncidents] = useState([]);

  useEffect(() => {
    getMyIncidents().then((res) => setIncidents(res.data));
  }, []);

  const count = (...statuses) => incidents.filter((i) => statuses.includes(i.status)).length;

  return (
    <div>
      <div className="cards">
        <Stat label="Mes incidents" value={incidents.length} />
        <Stat label="OPEN" value={count("OPEN")} />
        <Stat label="IN_PROGRESS" value={count("IN_PROGRESS")} />
        <Stat label="RESOLVED / CLOSED" value={count("RESOLVED", "CLOSED")} />
      </div>
      <h3>Derniers incidents</h3>
      <IncidentTable incidents={incidents.slice(0, 5)} />
    </div>
  );
}

function TechnicianDashboard() {
  const [incidents, setIncidents] = useState([]);

  useEffect(() => {
    getAssignedIncidents().then((res) => setIncidents(res.data));
  }, []);

  const count = (...statuses) => incidents.filter((i) => statuses.includes(i.status)).length;

  return (
    <div className="cards">
      <Stat label="Incidents affectés" value={incidents.length} />
      <Stat label="Critiques" value={incidents.filter((i) => i.priority === "CRITICAL").length} />
      <Stat label="En cours" value={count("IN_PROGRESS")} />
      <Stat label="Résolus" value={count("RESOLVED", "CLOSED")} />
    </div>
  );
}

function AdminDashboard() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    getStatistics().then((res) => setStats(res.data));
  }, []);

  if (!stats) return <p>Chargement...</p>;

  return (
    <div className="cards">
      <Stat label="Utilisateurs" value={stats.totalUsers} />
      <Stat label="Employés" value={stats.employees} />
      <Stat label="Techniciens" value={stats.technicians} />
      <Stat label="Incidents" value={stats.totalIncidents} />
      <Stat label="OPEN" value={stats.openIncidents} />
      <Stat label="IN_PROGRESS" value={stats.inProgressIncidents} />
      <Stat label="CLOSED" value={stats.closedIncidents} />
    </div>
  );
}

export default function Dashboard() {
  const role = getRole();

  return (
    <div>
      <h2>Tableau de bord</h2>
      <p>Bienvenue {getFullName()}</p>
      {role === "EMPLOYEE" && <EmployeeDashboard />}
      {role === "TECHNICIAN" && <TechnicianDashboard />}
      {role === "ADMIN" && <AdminDashboard />}
    </div>
  );
}
