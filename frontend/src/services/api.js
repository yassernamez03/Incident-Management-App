import axios from "axios";

const api = axios.create({ baseURL: "http://localhost:8080" });

// Avant chaque requête : ajoute automatiquement "Authorization: Bearer <JWT>"
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = "Bearer " + token;
  }
  return config;
});

// Après chaque réponse : si 401 (token expiré ou invalide) -> retour à la page Login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const isAuthRequest = error.config?.url?.startsWith("/auth/");
    if (error.response?.status === 401 && !isAuthRequest) {
      localStorage.clear();
      window.location.href = "/login?expired=1";
    }
    return Promise.reject(error);
  }
);

// Récupère le message d'erreur renvoyé par le backend
export function getErrorMessage(error) {
  return error.response?.data?.message || "Erreur de connexion au serveur";
}

export default api;
