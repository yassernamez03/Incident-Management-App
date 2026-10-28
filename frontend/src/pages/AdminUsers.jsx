import { useEffect, useState } from "react";
import { getUsers, enableUser, disableUser } from "../services/incidentService";
import { getErrorMessage } from "../services/api";

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");

  function loadUsers() {
    getUsers()
      .then((res) => setUsers(res.data))
      .catch((err) => setError(getErrorMessage(err)));
  }

  useEffect(loadUsers, []);

  async function toggle(user) {
    try {
      if (user.enabled) await disableUser(user.id);
      else await enableUser(user.id);
      loadUsers();
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  return (
    <div>
      <h2>Utilisateurs</h2>
      {error && <p className="error">{error}</p>}

      <table>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Email</th>
            <th>Rôle</th>
            <th>Actif</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {users.map((u) => (
            <tr key={u.id}>
              <td>{u.fullName}</td>
              <td>{u.email}</td>
              <td>{u.role}</td>
              <td>{u.enabled ? "Oui" : "Non"}</td>
              <td>
                <button onClick={() => toggle(u)}>{u.enabled ? "Désactiver" : "Activer"}</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
