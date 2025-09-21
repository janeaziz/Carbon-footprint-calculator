import React, { useState, useEffect } from 'react';
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL;

const UserManager = () => {
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [editingUser, setEditingUser] = useState(null);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'ascending' });
  const token = sessionStorage.getItem('token');

  useEffect(() => {
    if (token) fetchUsers();
  }, [token]);

  const fetchUsers = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await axios.get(`${API_BASE_URL}/api/admin/users`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUsers(response.data.map(user => ({
        id: user.id,
        email: user.email,
        nom: user.nom,
        role: user.role ? user.role.toLowerCase() : 'visiteur'
      })));
    } catch (e) {
      console.error("Failed to fetch users:", e);
      setError(`Impossible de charger les utilisateurs. ${e.response?.data?.message || e.message}`);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEditChange = (e) => {
    setEditingUser({ ...editingUser, [e.target.name]: e.target.value });
  };

  const updateUser = async () => {
    if (!editingUser.email || !editingUser.role) {
      alert("Veuillez remplir tous les champs !");
      return;
    }

    setError(null);
    const updatePayload = {
      email: editingUser.email,
      nom: editingUser.nom,
      roles: editingUser.role.charAt(0).toUpperCase() + editingUser.role.slice(1)
    };

    try {
      await axios.put(`${API_BASE_URL}/api/admin/users/${editingUser.id}`, updatePayload, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setEditingUser(null);
      fetchUsers();
    } catch (e) {
      console.error("Failed to update user:", e);
      setError(`Impossible de mettre à jour l'utilisateur. ${e.response?.data?.message || e.message}`);
    }
  };

  const deleteUser = async (userId) => {
    setError(null);
    try {
      await axios.delete(`${API_BASE_URL}/api/admin/users/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchUsers();
    } catch (e) {
      console.error("Failed to delete user:", e);
      setError(`Impossible de supprimer l'utilisateur. ${e.response?.data?.message || e.message}`);
    }
  };

  const startEditing = (user) => {
    setEditingUser({ ...user });
  };

  const cancelEditing = () => {
    setEditingUser(null);
  };

  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const getSortedUsers = () => {
    if (!sortConfig.key) return users;

    return [...users].sort((a, b) => {
      if (a[sortConfig.key] < b[sortConfig.key]) return sortConfig.direction === 'ascending' ? -1 : 1;
      if (a[sortConfig.key] > b[sortConfig.key]) return sortConfig.direction === 'ascending' ? 1 : -1;
      return 0;
    });
  };

  const getSortIndicator = (key) => {
    if (sortConfig.key !== key) return '⇅';
    return sortConfig.direction === 'ascending' ? '▲' : '▼';
  };

  const sortedUsers = getSortedUsers();

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 to-white dark:from-gray-900 dark:to-gray-950 px-4 py-8">
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-lg border border-gray-300 dark:border-gray-700">
        <h3 className="text-xl font-bold mb-4 text-center text-gray-800 dark:text-white">
          Gestion des utilisateurs
        </h3>

        {isLoading && <p className="text-center text-gray-600 dark:text-gray-300">Chargement...</p>}
        {error && <p className="text-center text-red-500">{error}</p>}

        <div className="overflow-x-auto">
          <table className="w-full border border-gray-300 dark:border-gray-700 shadow rounded text-black dark:text-white">
            <thead>
              <tr className="bg-gray-300 dark:bg-gray-800 text-center">
                {['email', 'nom', 'role'].map((key) => (
                  <th key={key} className="p-3">
                    <div className="flex items-center justify-center gap-1">
                      <span>{key.charAt(0).toUpperCase() + key.slice(1)}</span>
                      <button
                        onClick={() => requestSort(key)}
                        className="p-1 text-sm bg-gray-200 dark:bg-gray-700 rounded hover:bg-gray-400 dark:hover:bg-gray-600"
                      >
                        {getSortIndicator(key)}
                      </button>
                    </div>
                  </th>
                ))}
                <th className="p-3">Actions</th>
              </tr>
            </thead>
            <tbody>
              {sortedUsers.length > 0 ? (
                sortedUsers.map((user) => (
                  <tr key={user.id} className="text-center bg-white dark:bg-gray-900 border-b dark:border-gray-700">
                    <td className="p-3">
                      {editingUser?.id === user.id ? (
                        <input
                          type="email"
                          name="email"
                          value={editingUser.email}
                          onChange={handleEditChange}
                          className="p-2 border rounded w-full bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
                        />
                      ) : user.email}
                    </td>
                    <td className="p-3">
                      {editingUser?.id === user.id ? (
                        <input
                          type="text"
                          name="nom"
                          value={editingUser.nom}
                          onChange={handleEditChange}
                          className="p-2 border rounded w-full bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
                        />
                      ) : user.nom}
                    </td>
                    <td className="p-3">
                      {editingUser?.id === user.id ? (
                        <select
                          name="role"
                          value={editingUser.role}
                          onChange={handleEditChange}
                          className="p-2 border rounded w-full bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
                        >
                          <option value="visiteur">Visiteur</option>
                          <option value="normal">Normal</option>
                          <option value="admin">Admin</option>
                        </select>
                      ) : user.role}
                    </td>
                    <td className="p-3 flex flex-wrap justify-center items-center gap-2">
                      {editingUser?.id === user.id ? (
                        <>
                          <button
                            className="bg-green-500 text-white px-4 py-1 rounded hover:bg-green-600"
                            onClick={updateUser}
                          >
                            ✓ Valider
                          </button>
                          <button
                            className="bg-gray-500 text-white px-4 py-1 rounded hover:bg-gray-600"
                            onClick={cancelEditing}
                          >
                            ✕ Annuler
                          </button>
                        </>
                      ) : (
                        <>
                          <button
                            className="bg-blue-500 text-white px-4 py-1 rounded hover:bg-blue-600"
                            onClick={() => startEditing(user)}
                          >
                            ✎ Modifier
                          </button>
                          <button
                            className="bg-red-500 text-white px-4 py-1 rounded hover:bg-red-600"
                            onClick={() => deleteUser(user.id)}
                          >
                            ❌ Supprimer
                          </button>
                        </>
                      )}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="p-3 text-center text-gray-500 dark:text-gray-400">
                    {isLoading ? 'Chargement...' : 'Aucun utilisateur trouvé.'}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default UserManager;
