import { Navigate, Outlet } from "react-router-dom";
import { getRole } from "../services/authService";

// Si le rôle de l'utilisateur n'est pas dans la liste "roles" -> retour à l'accueil
export default function RoleRoute({ roles }) {
  return roles.includes(getRole()) ? <Outlet /> : <Navigate to="/" />;
}
