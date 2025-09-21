import { useEffect, useState } from "react";
import axios from "axios";
import React from 'react';

const Historique = () => {
    const [searches, setSearches] = useState([]);
    const token = sessionStorage.getItem("token");

    useEffect(() => {
        if (!token) return;
        axios
            .get(`${import.meta.env.VITE_API_URL}/history`, {
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((res) => {
                console.log("🔍 Données historiques reçues :", res.data);
                setSearches(res.data);
            })
            .catch((err) => console.error("❌ Erreur historique :", err));
    }, [token]);

    const deleteTrajet = (id) => {
        if (!window.confirm("Supprimer ce trajet ?")) return;
        axios
            .delete(`${import.meta.env.VITE_API_URL}/history/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            })
            .then(() => setSearches((prev) => prev.filter((s) => s.id !== id)))
            .catch((err) => console.error("❌ Erreur suppression trajet :", err));
    };

    const recherches = searches.filter(
        (s) =>
            !s.contrainte?.toLowerCase().startsWith("simulation")
    );

    const simulations = searches.filter(
        (s) =>
            s.contrainte?.toLowerCase().startsWith("simulation")
    );

    return (
        <div className="min-h-screen bg-gray-100 dark:bg-gray-950 text-black dark:text-white transition-colors">
            <div className="max-w-4xl mx-auto mt-10 p-6 bg-white dark:bg-gray-800 shadow-lg rounded-lg border border-gray-300 dark:border-gray-700">
                <h2 className="text-3xl font-bold text-center mb-6">🕒 Historique</h2>

                {recherches.length > 0 && (
                    <>
                        <h3 className="text-xl font-semibold mb-4">🔍 Recherches</h3>
                        <ul className="space-y-4 mb-10">
                            {recherches.map((trajet) => (
                                <li key={trajet.id} className="bg-gray-100 dark:bg-gray-900 p-4 rounded shadow border border-gray-200 dark:border-gray-700">
                                    <div className="flex justify-between items-center">
                    <span>
                      {trajet.origine} → {trajet.destination} |{" "}
                        {new Date(trajet.date).toLocaleDateString()}
                    </span>
                                        <button
                                            onClick={() => deleteTrajet(trajet.id)}
                                            className="text-red-500 font-semibold hover:underline"
                                        >
                                            Supprimer
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </>
                )}

                {simulations.length > 0 && (
                    <>
                        <h3 className="text-xl font-semibold mb-4">📊 Simulations</h3>
                        <ul className="space-y-4">
                            {simulations.map((trajet) => (
                                <li key={trajet.id} className="bg-gray-100 dark:bg-gray-900 p-4 rounded shadow border border-gray-200 dark:border-gray-700">
                                    <div className="flex justify-between items-center">
                    <span>
                      {trajet.origine} → {trajet.destination} |{" "}
                        {new Date(trajet.date).toLocaleDateString()}
                    </span>
                                        <button
                                            onClick={() => deleteTrajet(trajet.id)}
                                            className="text-red-500 font-semibold hover:underline"
                                        >
                                            Supprimer
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </>
                )}

                {recherches.length === 0 && simulations.length === 0 && (
                    <p className="text-center text-gray-500 dark:text-gray-400">
                        Aucun trajet sauvegardé.
                    </p>
                )}
            </div>
        </div>
    );
};

export default Historique;
