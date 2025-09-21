import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";

const Search = () => {
  const [start, setStart] = useState("");
  const [destination, setDestination] = useState("");
  const [results, setResults] = useState([]);
  const [isSaved, setIsSaved] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const resultsRef = useRef(null);

  const token = sessionStorage.getItem("token");

  const performSearch = async () => {
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
      if (!res.ok) throw new Error("Erreur lors de la recherche");

      const data = await res.json();
      const formatted = data.map((d) => ({
        mode: d.mode,
        co2: d.co2,
        subMode: d.subMode || null,
        label: d.label || null,
        mapsUrl: d.mapsUrl || null,
        durationMinutes: d.durationMinutes || null,
        distanceLabel: d.distanceLabel || null,
      }));

      setResults(formatted);
      setIsSaved(false);
      setTimeout(() => {
        resultsRef.current?.scrollIntoView({ behavior: "smooth" });
      }, 300);
    } catch (err) {
      console.error(err);
      alert("❌ Aucune donnée trouvée pour ce trajet !");
    } finally {
      setLoading(false);
    }
  };

  const saveSearch = async () => {
    if (!token) {
      alert("Veuillez vous connecter pour sauvegarder un trajet.");
      return;
    }

    try {
      const trajetRes = await fetch(`${import.meta.env.VITE_API_URL}/trajets`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          origine: start,
          destination: destination,
          distance: 0,
          contrainte: "",
        }),
      });

      if (!trajetRes.ok) throw new Error("Erreur création trajet");
      const trajet = await trajetRes.json();

      await fetch(
        `${import.meta.env.VITE_API_URL}/history?trajetId=${trajet.id}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("✅ Trajet sauvegardé dans l'historique !");
      setIsSaved(true);
    } catch (err) {
      console.error(err);
      alert("Erreur lors de la sauvegarde dans l'historique !");
    }
  };

  const goToCompare = () => {
    navigate("/comparer", { state: { start, destination } });
  };

  const getEmojiForMode = (mode, subMode) => {
    const name = (subMode || mode || "").toLowerCase();
    if (name.includes("voiture")) return "🚗";
    if (name.includes("vélo") && name.includes("électrique")) return "⚡🚲";
    if (name.includes("vélo")) return "🚲";
    if (name.includes("marche") || name.includes("walk")) return "🚶";
    if (name.includes("subway") || name.includes("métro")) return "🚇";
    if (name.includes("tram")) return "🚊";
    if (name.includes("bus")) return "🚌";
    if (name.includes("train")) return "🚆";
    return "🚘";
  };

  const co2Min = Math.min(...results.map((r) => r.co2));
  const co2Max = Math.max(...results.map((r) => r.co2));

  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-100 to-white dark:from-gray-900 dark:to-black pb-10">
      <div className="max-w-5xl mx-auto mt-10 p-6 bg-white dark:bg-gray-800 shadow-xl rounded-lg border border-gray-300 dark:border-gray-700">
        <h2 className="text-3xl font-extrabold text-center mb-6 text-blue-900 dark:text-white">
          Rechercher un trajet
        </h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
          <input
            type="text"
            placeholder="Départ"
            className="p-3 border border-gray-300 dark:border-gray-600 rounded-lg w-full shadow-sm bg-white dark:bg-gray-900 text-black dark:text-white"
            value={start}
            onChange={(e) => setStart(e.target.value)}
          />
          <input
            type="text"
            placeholder="Destination"
            className="p-3 border border-gray-300 dark:border-gray-600 rounded-lg w-full shadow-sm bg-white dark:bg-gray-900 text-black dark:text-white"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
          />
        </div>

        <div className="flex justify-center gap-4 mt-4">
          <button
            onClick={performSearch}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-700 transition"
          >
            🔍 Rechercher
          </button>
        </div>

        {loading && (
          <div className="flex justify-center mt-6 text-blue-600 font-semibold animate-pulse dark:text-blue-300">
            🔄 Chargement des résultats en cours...
          </div>
        )}
        {!loading && results.length === 0 && (
          <div className="flex flex-col items-center mt-10 text-gray-600 dark:text-gray-300">
            {/* Light Mode */}
            <img
              src="/no-search.svg"
              alt="Recherche"
              className="w-52 h-52 opacity-90 dark:hidden"
            />

            {/* Drk Mode */}
            <img
              src="/no-search-dark.svg"
              alt="Recherche"
              className="w-52 h-52 opacity-90 hidden dark:block"
            />

            <p className="mt-4 text-lg font-semibold text-center">
              Commencez par rechercher un trajet ✨
            </p>
          </div>
        )}

        {results.length > 0 && (
          <div
            className="mt-6 bg-gray-50 dark:bg-gray-900 p-5 rounded-lg shadow-inner border border-gray-200 dark:border-gray-700"
            ref={resultsRef}
          >
            <h3 className="text-xl font-bold text-gray-800 dark:text-white mb-4">
              Résultats
            </h3>

            {results[0].mapsUrl && (
              <div className="flex justify-center mb-4">
                <a
                  href={results[0].mapsUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-full hover:bg-blue-700 transition"
                >
                  🗺️ Voir l’itinéraire sur Google Maps
                </a>
              </div>
            )}

            <div className="overflow-x-auto">
              <table className="w-full text-center border-collapse border border-gray-300 dark:border-gray-700 text-sm">
                <thead>
                  <tr className="bg-blue-100 dark:bg-blue-950 text-blue-800 dark:text-blue-100 uppercase text-sm">
                    <th className="border border-gray-300 dark:border-gray-600 p-3">
                      Mode
                    </th>
                    <th className="border border-gray-300 dark:border-gray-600 p-3">
                      Détail
                    </th>
                    <th className="border border-gray-300 dark:border-gray-600 p-3">
                      Distance
                    </th>
                    <th className="border border-gray-300 dark:border-gray-600 p-3">
                      Durée
                    </th>
                    <th className="border border-gray-300 dark:border-gray-600 p-3">
                      Émissions
                    </th>
                    <th className="border border-gray-300 dark:border-gray-600 p-3">
                      Économie CO₂
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {results.map((result, index) => {
                    const isBest = result.co2 === co2Min;
                    const co2Diff = co2Max - result.co2;

                    return (
                      <tr
                        key={index}
                        className={`bg-white dark:bg-gray-800 ${
                          isBest
                            ? "border-4 border-green-400"
                            : "border border-gray-200 dark:border-gray-600"
                        }`}
                      >
                        <td className="p-2 font-medium">
                          {getEmojiForMode(result.mode, result.subMode)}{" "}
                          {result.mode}
                        </td>
                        <td className="p-2">{result.label}</td>
                        <td className="p-2 text-gray-700 dark:text-gray-200 font-mono">
                          {result.distanceLabel || "-"}
                        </td>
                        <td className="p-2 text-gray-700 dark:text-gray-200 font-mono">
                          {result.durationMinutes?.toFixed(2) ?? "-"} min
                        </td>
                        <td
                          className={`p-2 font-bold ${
                            result.co2 === 0
                              ? "text-green-600"
                              : result.co2 < 10000
                              ? "text-yellow-500"
                              : "text-red-600"
                          }`}
                        >
                          {result.co2.toFixed(2)} g CO₂
                        </td>
                        <td className="p-2 text-gray-600 dark:text-gray-300">
                          {isBest ? "✔ Meilleur" : `-${co2Diff.toFixed(2)} g`}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>

            <div className="flex justify-center gap-4 mt-6">
              {token && !isSaved && (
                <button
                  onClick={saveSearch}
                  className="bg-green-500 text-white px-5 py-3 rounded-lg hover:bg-green-600"
                >
                  ⭐ Sauvegarder
                </button>
              )}
              <button
                onClick={goToCompare}
                className="bg-purple-600 text-white px-5 py-3 rounded-lg hover:bg-purple-700"
              >
                🔄 Comparer
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Search;
