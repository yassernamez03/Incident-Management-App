import api from "./api";

// ----- Incidents -----
export const createIncident = (incident) => api.post("/api/incidents", incident);
export const getMyIncidents = () => api.get("/api/incidents/my");
export const getAssignedIncidents = () => api.get("/api/incidents/assigned");
export const getAllIncidents = () => api.get("/api/incidents");
export const getIncident = (id) => api.get("/api/incidents/" + id);
export const updateStatus = (id, status) => api.put("/api/incidents/" + id + "/status", { status });
export const assignIncident = (id, technicianId) => api.put("/api/incidents/" + id + "/assign/" + technicianId);
export const addComment = (id, content) => api.post("/api/incidents/" + id + "/comments", { content });

// ----- Administration -----
export const getUsers = () => api.get("/api/admin/users");
export const enableUser = (id) => api.put("/api/admin/users/" + id + "/enable");
export const disableUser = (id) => api.put("/api/admin/users/" + id + "/disable");
export const getStatistics = () => api.get("/api/admin/statistics");
