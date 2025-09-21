import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import ComparisonTable from "../components/ComparisonTable.jsx";
import ComparisonChart from "../components/ComparisonChart.jsx";

const Compare = () => {
  const location = useLocation();
  const [start, setStart] = useState(location.state?.start || "");
  const [destination, setDestination] = useState(
    location.state?.destination || ""
  );
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [distance, setDistance] = useState(null);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }, []);

  useEffect(() => {
    if (start && destination) {
      fetchTransportData();
    }
  }, [start, destination]);

  const fetchTransportData = async () => {
    if (!start.trim() || !destination.trim()) {
      alert("Veuillez entrer un départ et une destination !");
      return;
    }

    setLoading(true);
    try {
      const res = await fetch(
        `${
          import.meta.env.VITE_API_URL
        }/transports/search?origine=${start}&destination=${destination}`
      );

      if (!res.ok)
        throw new Error("Erreur lors de la récupération des données");

      const data = await res.json();

      const voiture = data.find((d) =>
        d.mode.toLowerCase().includes("voiture")
      );
      const velo = data.find((d) => d.mode.toLowerCase().includes("vélo"));
      const transportCommun = data.find((d) =>
        d.mode.toLowerCase().includes("commun")
      );

      const formatDistanceLabel = (label) => {
        if (!label) return "-";
        return label
          .split("+")
          .map((s) => s.trim())
          .map((entry) => {
            const match = entry.match(/\(([\d.,]+)\s*km\)/i);
            const dist = match ? match[1] : "?";
            const mode = entry.replace(/\([^()]{1,50}\)/g, "").trim();
            return `${mode} (${dist} km)`;
          })
          .join(" + ");
      };

      const filtered = [];

      if (voiture) {
        filtered.push({
          id: 1,
          name: voiture.mode,
          co2: voiture.co2,
          time: voiture.durationMinutes,
          distance: voiture.distanceKm,
          label: voiture.label,
          consommationEnergie: voiture.consommationEnergie,
          unite: voiture.unite,
          prixEstime: voiture.prixEstime,
          comfort: "-",
          noise: "-",
        });
      }

      if (velo) {
        filtered.push({
          id: 2,
          name: velo.mode,
          co2: velo.co2,
          time: velo.durationMinutes,
          distance: velo.distanceKm,
          label: velo.label,
          consommationEnergie: velo.consommationEnergie,
          unite: velo.unite,
          prixEstime: velo.prixEstime,
          comfort: "-",
          noise: "-",
        });
      }

      if (transportCommun) {
        filtered.push({
          id: 3,
          name: transportCommun.mode,
          co2: transportCommun.co2,
          time: transportCommun.durationMinutes,
          distance: formatDistanceLabel(transportCommun.label),
          label: transportCommun.label,
          consommationEnergie: transportCommun.consommationEnergie,
          unite: transportCommun.unite,
          prixEstime: transportCommun.prixEstime,
          comfort: "-",
          noise: "-",
        });
      }

      setResults(filtered);
      setDistance(
        voiture?.distanceKm ??
          velo?.distanceKm ??
          transportCommun?.distanceKm ??
          null
      );
    } catch (err) {
      console.error(err);
      alert("❌ Aucune donnée trouvée !");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 dark:bg-gray-950 text-black dark:text-white transition-colors">
      <div className="max-w-4xl mx-auto mt-10 p-6 bg-white dark:bg-gray-800 shadow-lg rounded-lg border border-gray-300 dark:border-gray-700">
        <h2 className="text-3xl font-bold text-center mb-6">
          Comparer les transports
        </h2>

        <div className="flex flex-col md:flex-row justify-center gap-4 mb-4">
          <input
            type="text"
            placeholder="Départ"
            className="p-3 border rounded-lg w-full bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
            value={start}
            onChange={(e) => setStart(e.target.value)}
          />
          <input
            type="text"
            placeholder="Destination"
            className="p-3 border rounded-lg w-full bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
          />
        </div>

        <div className="flex justify-center gap-4">
          <button
            className="bg-blue-500 text-white px-5 py-3 rounded-lg hover:bg-blue-600 transition"
            onClick={fetchTransportData}
          >
            🔍 Rechercher
          </button>
        </div>

        {loading && (
          <div className="text-center text-blue-500 mt-6 animate-pulse">
            Chargement des données...
          </div>
        )}

        {distance && (
          <p className="text-center mt-6 text-lg font-semibold">
            Distance estimée : {distance.toFixed(2)} km
          </p>
        )}

        {results.length > 1 ? (
          <>
            <ComparisonTable selectedOptions={results} />
            <ComparisonChart selectedOptions={results} />
          </>
        ) : (
          <p className="text-center text-gray-500 dark:text-gray-400 mt-4">
            Lancez une recherche pour afficher les résultats.
          </p>
        )}
      </div>
    </div>
  );
};

export default Compare;
