import { useEffect, useState } from "react";
import { getProfile, updateProfile, changePassword } from "../services/authService";
import { getErrorMessage } from "../services/api";

export default function Profile() {
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [profileMsg, setProfileMsg] = useState("");
  const [passwordMsg, setPasswordMsg] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getProfile().then((res) => {
      setFullName(res.data.fullName);
      setEmail(res.data.email);
    });
  }, []);

  async function handleProfile(e) {
    e.preventDefault();
    setProfileMsg("");
    setError("");
    try {
      await updateProfile(fullName, email);
      setProfileMsg("Profil modifié");
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  async function handlePassword(e) {
    e.preventDefault();
    setPasswordMsg("");
    setError("");
    try {
      await changePassword(oldPassword, newPassword, confirmPassword);
      setPasswordMsg("Mot de passe modifié");
      setOldPassword("");
      setNewPassword("");
      setConfirmPassword("");
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  return (
    <div>
      <h2>Mon profil</h2>
      {error && <p className="error">{error}</p>}

      <form onSubmit={handleProfile}>
        <input placeholder="Nom complet" value={fullName} onChange={(e) => setFullName(e.target.value)} />
        <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <button type="submit">Enregistrer</button>
        {profileMsg && <span className="success">{profileMsg}</span>}
      </form>

      <h3>Changer le mot de passe</h3>
      <form onSubmit={handlePassword}>
        <input type="password" placeholder="Ancien mot de passe" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)} />
        <input type="password" placeholder="Nouveau mot de passe" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
        <input type="password" placeholder="Confirmer le nouveau mot de passe" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
        <button type="submit">Changer le mot de passe</button>
        {passwordMsg && <span className="success">{passwordMsg}</span>}
      </form>
    </div>
  );
}
