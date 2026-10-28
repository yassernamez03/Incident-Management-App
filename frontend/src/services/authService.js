import api from "./api";

// Le token, le rôle et le nom sont stockés dans le navigateur (localStorage)
export function saveUser(data) {
  localStorage.setItem("token", data.token);
  localStorage.setItem("role", data.role);
  localStorage.setItem("fullName", data.fullName);
}

export function getToken() { return localStorage.getItem("token"); }
export function getRole() { return localStorage.getItem("role"); }
export function getFullName() { return localStorage.getItem("fullName"); }

export function logout() { localStorage.clear(); }

export async function login(email, password) {
  const res = await api.post("/auth/login", { email, password });
  saveUser(res.data);
}

export function register(fullName, email, password) {
  return api.post("/auth/register", { fullName, email, password });
}

// ----- Profil -----
export function getProfile() {
  return api.get("/api/profile");
}

export async function updateProfile(fullName, email) {
  const res = await api.put("/api/profile", { fullName, email });
  saveUser(res.data); // nouveau token car l'email peut avoir changé
}

export function changePassword(oldPassword, newPassword, confirmPassword) {
  return api.put("/api/profile/password", { oldPassword, newPassword, confirmPassword });
}
