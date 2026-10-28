import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createIncident } from "../services/incidentService";
import { getErrorMessage } from "../services/api";

export default function NewIncident() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("OTHER");
  const [priority, setPriority] = useState("MEDIUM");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await createIncident({ title, description, category, priority });
      navigate("/my-incidents");
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  return (
    <div>
      <h2>Déclarer un incident</h2>
      {error && <p className="error">{error}</p>}

      <form onSubmit={handleSubmit}>
        <input placeholder="Titre" value={title} onChange={(e) => setTitle(e.target.value)} />
        <textarea placeholder="Description" rows="4" value={description} onChange={(e) => setDescription(e.target.value)} />

        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          {["NETWORK", "MALWARE", "PHISHING", "ACCESS", "SOFTWARE", "HARDWARE", "DATA_LEAK", "OTHER"].map((c) => (
            <option key={c}>{c}</option>
          ))}
        </select>

        <select value={priority} onChange={(e) => setPriority(e.target.value)}>
          {["LOW", "MEDIUM", "HIGH", "CRITICAL"].map((p) => (
            <option key={p}>{p}</option>
          ))}
        </select>

        <button type="submit">Envoyer</button>
      </form>
    </div>
  );
}
