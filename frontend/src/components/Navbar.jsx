import { Link, useLocation, useNavigate } from "react-router-dom";
import { getToken, getRole, getFullName, logout } from "../services/authService";

export default function Navbar() {
  useLocation(); // pour réafficher la barre à chaque changement de page
  const navigate = useNavigate();
  const role = getRole();

  function handleLogout() {
    logout();
    navigate("/login");
  }

  if (!getToken()) return null;

  return (
    <div className="navbar">
      <Link to="/">Dashboard</Link>

      {role === "EMPLOYEE" && <Link to="/my-incidents">Mes incidents</Link>}
      {role === "EMPLOYEE" && <Link to="/new-incident">Nouvel incident</Link>}
      {role === "TECHNICIAN" && <Link to="/assigned">Incidents affectés</Link>}
      {role === "ADMIN" && <Link to="/admin/incidents">Incidents</Link>}
      {role === "ADMIN" && <Link to="/admin/users">Utilisateurs</Link>}

      <span className="spacer"></span>
      <Link to="/profile">{getFullName()} ({role})</Link>
      <button onClick={handleLogout}>Déconnexion</button>
    </div>
  );
}
