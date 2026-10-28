import { Navigate, Outlet } from "react-router-dom";
import { getToken } from "../services/authService";

// Si l'utilisateur n'est pas connecté -> page Login
export default function ProtectedRoute() {
  return getToken() ? <Outlet /> : <Navigate to="/login" />;
}
