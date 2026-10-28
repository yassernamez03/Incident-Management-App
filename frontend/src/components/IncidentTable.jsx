import { Link } from "react-router-dom";

// Tableau d'incidents réutilisé dans plusieurs pages
export default function IncidentTable({ incidents }) {
  if (incidents.length === 0) return <p>Aucun incident.</p>;

  return (
    <table>
      <thead>
        <tr>
          <th>Titre</th>
          <th>Catégorie</th>
          <th>Priorité</th>
          <th>Statut</th>
          <th>Créé par</th>
          <th>Affecté à</th>
          <th>Date</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {incidents.map((i) => (
          <tr key={i.id}>
            <td>{i.title}</td>
            <td>{i.category}</td>
            <td>{i.priority}</td>
            <td>{i.status}</td>
            <td>{i.createdBy}</td>
            <td>{i.assignedTo || "-"}</td>
            <td>{new Date(i.createdAt).toLocaleString()}</td>
            <td><Link to={"/incidents/" + i.id}>Détails</Link></td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
