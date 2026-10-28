import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../services/authService";
import { getErrorMessage } from "../services/api";

export default function Register() {
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await register(fullName, email, password);
      navigate("/login?registered=1");
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  return (
    <div>
      <h2>Créer un compte</h2>
      {error && <p className="error">{error}</p>}

      <form onSubmit={handleSubmit}>
        <input placeholder="Nom complet" value={fullName} onChange={(e) => setFullName(e.target.value)} />
        <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input type="password" placeholder="Mot de passe (6 caractères min.)" value={password} onChange={(e) => setPassword(e.target.value)} />
        <button type="submit">Créer le compte</button>
      </form>

      <p>Déjà un compte ? <Link to="/login">Se connecter</Link></p>
    </div>
  );
}
