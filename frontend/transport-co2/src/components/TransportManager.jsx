// ✅ TransportManager.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL;

const TransportManager = () => {
  const [transports, setTransports] = useState([]);
  const [newTransport, setNewTransport] = useState({ name: "", co2: "" });
  const [editingTransport, setEditingTransport] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'ascending' });
  const token = sessionStorage.getItem('token');

  useEffect(() => {
    if (token) fetchTransports();
  }, [token]);

  const fetchTransports = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await axios.get(`${API_BASE_URL}/api/admin/transports`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setTransports(response.data.map(t => ({
        id: t.id,
        name: t.nom,
        co2: t.consommationMoyenne
      })));
    } catch (e) {
      setError(`Impossible de charger les transports. ${e.response?.data?.message || e.message}`);
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    setNewTransport({ ...newTransport, [e.target.name]: e.target.value });
  };

  const handleEditChange = (e) => {
    setEditingTransport({ ...editingTransport, [e.target.name]: e.target.value });
  };

  const addTransport = async () => {
    const { name, co2 } = newTransport;
    if (!name || !co2) return alert("Veuillez remplir tous les champs !");

    try {
      await axios.post(`${API_BASE_URL}/api/admin/transports`, {
        nom: name,
        consommationMoyenne: parseFloat(co2)
      }, { headers: { Authorization: `Bearer ${token}` } });
      setNewTransport({ name: "", co2: "" });
      fetchTransports();
    } catch (e) {
      setError(`Erreur: ${e.response?.data?.message || e.message}`);
    }
  };

  const updateTransport = async () => {
    const { name, co2 } = editingTransport;
    if (!name || !co2) return alert("Veuillez remplir tous les champs !");

    try {
      await axios.put(`${API_BASE_URL}/api/admin/transports/${editingTransport.id}`, {
        nom: name,
        consommationMoyenne: parseFloat(co2)
      }, { headers: { Authorization: `Bearer ${token}` } });
      setEditingTransport(null);
      fetchTransports();
    } catch (e) {
      setError(`Erreur: ${e.response?.data?.message || e.message}`);
    }
  };

  const deleteTransport = async (id) => {
    try {
      await axios.delete(`${API_BASE_URL}/api/admin/transports/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchTransports();
    } catch (e) {
      setError(`Erreur: ${e.response?.data?.message || e.message}`);
    }
  };

  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') direction = 'descending';
    setSortConfig({ key, direction });
  };

  const sortedTransports = [...transports].sort((a, b) => {
    if (!sortConfig.key) return 0;
    const aVal = sortConfig.key === 'name' ? a[sortConfig.key].toLowerCase() : parseFloat(a[sortConfig.key]);
    const bVal = sortConfig.key === 'name' ? b[sortConfig.key].toLowerCase() : parseFloat(b[sortConfig.key]);
    return aVal < bVal ? -1 : aVal > bVal ? 1 : 0;
  });

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 to-white dark:from-gray-900 dark:to-gray-950 px-4 py-8 text-black dark:text-white">
      <h3 className="text-xl font-bold text-center mb-4">Gestion des transports</h3>
      {error && <p className="text-center text-red-500">{error}</p>}
      <div className="overflow-x-auto">
        <table className="w-full border border-gray-300 dark:border-gray-700 shadow-lg rounded-lg">
          <thead className="bg-gray-300 dark:bg-gray-800 text-black dark:text-white">
            <tr>
              <th className="p-3">Nom</th>
              <th className="p-3">CO₂ (g/km)</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {sortedTransports.map(t => (
              <tr key={t.id} className="bg-white dark:bg-gray-900 border-b dark:border-gray-700 text-center">
                <td className="p-3">{editingTransport?.id === t.id ? <input name="name" value={editingTransport.name} onChange={handleEditChange} className="w-full p-2 border rounded bg-white dark:bg-gray-800 text-black dark:text-white" /> : t.name}</td>
                <td className="p-3">{editingTransport?.id === t.id ? <input name="co2" value={editingTransport.co2} onChange={handleEditChange} className="w-full p-2 border rounded bg-white dark:bg-gray-800 text-black dark:text-white" /> : `${t.co2} g/km`}</td>
                <td className="p-3 flex justify-center gap-2">
                  {editingTransport?.id === t.id ? (
                    <>
                      <button onClick={updateTransport} className="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded">✓</button>
                      <button onClick={() => setEditingTransport(null)} className="bg-gray-500 hover:bg-gray-600 text-white px-3 py-1 rounded">✕</button>
                    </>
                  ) : (
                    <>
                      <button onClick={() => setEditingTransport(t)} className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded">✎</button>
                      <button onClick={() => deleteTransport(t.id)} className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded">🗑</button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="mt-6 grid grid-cols-3 gap-4">
        <input name="name" value={newTransport.name} onChange={handleChange} placeholder="Nom transport" className="p-2 border rounded bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600" />
        <input name="co2" value={newTransport.co2} onChange={handleChange} placeholder="CO₂ g/km" className="p-2 border rounded bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600" />
        <button onClick={addTransport} className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded">➕ Ajouter</button>
      </div>
    </div>
  );
};

export default TransportManager;
