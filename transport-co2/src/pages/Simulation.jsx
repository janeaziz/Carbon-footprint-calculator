import React, { useState, useRef } from "react";
import axios from "axios";
import SimulationChart from "../components/SimulationChart.jsx";

const Simulation = () => {
  const [start, setStart] = useState("");
  const [destination, setDestination] = useState("");
  const [selectedTransport, setSelectedTransport] = useState(null);
  const [frequency, setFrequency] = useState("daily");
  const [duration, setDuration] = useState(30);
  const [trips, setTrips] = useState([]);
  const [transportOptions, setTransportOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [processing, setProcessing] = useState(false);
  const token = sessionStorage.getItem("token");
  const resultsRef = useRef(null);

  const fetchTransportOptions = async () => {
    if (!start || !destination) return;

    setTrips([]);
    setSelectedTransport(null);
    setLoading(true);
    setProcessing(true);

    try {
      const res = await fetch(
        `${
          import.meta.env.VITE_API_URL
        }/transports/search?origine=${start}&destination=${destination}`
      );

      if (!res.ok)
        throw new Error("Erreur lors de la récupération des transports");

      const data = await res.json();

      let formatted = data.map((d, index) => {
        const distanceStr = d.distance ? `${d.distance.toFixed(2)} km` : "";
        const cleanName = d.subMode
          ? `${d.subMode} ${distanceStr}`.trim()
          : d.mode;

        return {
          id: index + 1,
          name: cleanName,
          co2: d.co2,
          distance: d.distance,
          label: d.label || "-",
          subMode: d.subMode || "-",
        };
      });

      const isPublic = (name) =>
        name.toLowerCase().includes("transport en commun");

      const publicOptions = formatted.filter((opt) => isPublic(opt.name));
      const otherOptions = formatted.filter((opt) => !isPublic(opt.name));

      const groupedPublic = {};
      publicOptions.forEach((opt) => {
        const key = opt.label;
        if (!groupedPublic[key] || opt.co2 < groupedPublic[key].co2) {
          groupedPublic[key] = opt;
        }
      });

      const bestPublic = Object.values(groupedPublic);
      const merged = [...otherOptions, ...bestPublic];
      const maxEmission = Math.max(...merged.map((o) => o.co2));
      let markedBest = false;
      let lastKeptCO2 = null;

      const sorted = merged
        .sort((a, b) => a.co2 - b.co2)
        .filter((o) => {
          if (lastKeptCO2 === null) {
            lastKeptCO2 = o.co2;
            return true;
          }
          const diff = Math.abs(o.co2 - lastKeptCO2);
          const percent = diff / lastKeptCO2;
          if (percent >= 0.01) {
            lastKeptCO2 = o.co2;
            return true;
          }
          return false;
        })
        .map((o) => {
          const saved = maxEmission > 0 ? maxEmission - o.co2 : 0;
          const savedPercent =
            maxEmission > 0 ? ((saved / maxEmission) * 100).toFixed(1) : null;

          let isBest = false;
          if (!markedBest && o.co2 > 0) {
            isBest = true;
            markedBest = true;
          }

          return {
            ...o,
            isEcoFriendly: isBest,
            isWorst: o.co2 === maxEmission,
            saved: saved > 0 ? saved.toFixed(2) : null,
            savedPercent: savedPercent,
          };
        });

      setTransportOptions(sorted);

      // ✅ Auto-select most eco-friendly (if co2 > 0)
      const best = sorted.find((opt) => opt.isEcoFriendly);
      if (best) {
        setSelectedTransport(best);
      }

      // ✅ Scroll to results
      setTimeout(() => {
        if (resultsRef.current) {
          resultsRef.current.scrollIntoView({ behavior: "smooth" });
        }
        setProcessing(false);
      }, 150);
    } catch (err) {
      console.error(err);
      setTransportOptions([]);
      setProcessing(false);
    }

    setLoading(false);
  };

  const handleSelectTransport = (option) => {
    setSelectedTransport(option);
  };

  const addTrip = () => {
    if (!selectedTransport || !start.trim() || !destination.trim()) {
      alert(
        "❌ Veuillez entrer un départ, une destination et sélectionner un transport !"
      );
      return;
    }

    const newTrip = {
      start,
      destination,
      name: selectedTransport.name,
      co2: selectedTransport.co2,
      frequency,
      duration,
    };

    setTrips((prevTrips) => [...prevTrips, newTrip]);
    setSelectedTransport(null); // keep departure/destination
  };

  const handleSaveSimulations = async () => {
    try {
      const token = sessionStorage.getItem("token");
      if (!token) {
        alert("Veuillez vous connecter pour sauvegarder.");
        return;
      }

      for (const trip of trips) {
        // 1. Créer le trajet
        const trajetRes = await fetch(
          `${import.meta.env.VITE_API_URL}/trajets`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
              origine: trip.start,
              destination: trip.destination,
              distance: 0, // ou utilise `trip.distance` si tu l’as
              contrainte: `simulation: ${trip.name} (${trip.frequency}, ${trip.duration} jours)`,
            }),
          }
        );

        if (!trajetRes.ok) throw new Error("Erreur création trajet");
        const trajet = await trajetRes.json();

        // 2. Ajouter à l’historique
        await fetch(
          `${import.meta.env.VITE_API_URL}/history?trajetId=${trajet.id}`,
          {
            method: "POST",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
      }

      alert("✅ Simulations sauvegardées et ajoutées à l'historique !");
      setTrips([]);
      setSelectedTransport(null);
    } catch (error) {
      console.error("Erreur lors de la sauvegarde :", error);
      alert("❌ Échec de la sauvegarde !");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 dark:bg-gray-950 text-black dark:text-white transition-colors">
      <div className="max-w-3xl mx-auto mt-10 p-6 bg-white dark:bg-gray-800 shadow-lg rounded-lg border border-gray-300 dark:border-gray-700">
        <h2 className="text-3xl font-bold text-center mb-6">
          Simulation d'empreinte carbone
        </h2>

        <div className="mb-4">
          <input
            type="text"
            placeholder="Départ"
            className="p-3 border border-gray-300 dark:border-gray-600 rounded-lg w-full mb-2 bg-white dark:bg-gray-900 text-black dark:text-white"
            value={start}
            onChange={(e) => setStart(e.target.value)}
          />
          <input
            type="text"
            placeholder="Destination"
            className="p-3 border border-gray-300 dark:border-gray-600 rounded-lg w-full bg-white dark:bg-gray-900 text-black dark:text-white"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
          />
        </div>

        <div className="flex justify-center mb-6">
          <button
            className="bg-blue-600 text-white px-5 py-3 rounded-lg hover:bg-blue-700 transition disabled:opacity-50"
            onClick={fetchTransportOptions}
            disabled={!start.trim() || !destination.trim() || loading}
          >
            🔍 Trouver transport
          </button>
        </div>

        {loading && (
          <p className="text-center text-sm text-gray-500 dark:text-gray-300 mb-4">
            ⏳ Recherche des transports en cours...
          </p>
        )}

        {!loading && processing && (
          <p className="text-center text-sm text-gray-500 dark:text-gray-300 mb-4">
            🛠️ Traitement des résultats...
          </p>
        )}

        <div
          ref={resultsRef}
          className={`grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-4 transition-opacity duration-700 ${
            transportOptions.length > 0 && !loading
              ? "opacity-100"
              : "opacity-0"
          }`}
        >
          {transportOptions.map((option) => (
            <button
              key={option.id}
              onClick={() =>
                setSelectedTransport(
                  selectedTransport?.id === option.id ? null : option
                )
              }
              disabled={loading}
              className={`p-4 border rounded-xl shadow-md text-left transition relative overflow-hidden 
                ${
                  selectedTransport?.id === option.id
                    ? "bg-blue-500 text-white"
                    : "bg-white dark:bg-gray-900 text-gray-800 dark:text-gray-200 hover:bg-blue-100 dark:hover:bg-gray-800"
                } 
                ${option.isEcoFriendly ? "ring-2 ring-green-500" : ""}
                ${option.isWorst ? "ring-2 ring-red-500" : ""}
              `}
            >
              <div className="font-bold text-lg mb-1">{option.name}</div>
              {option.isEcoFriendly && option.co2 > 0 && (
                <div className="text-green-500 font-medium text-sm mb-1">
                  🌿 Meilleur choix écologique
                </div>
              )}
              <div className="text-2xl font-bold text-blue-700 dark:text-blue-300">
                {option.co2.toFixed(2)} g CO₂
              </div>
              {option.isWorst && (
                <div className="text-red-600 text-sm mt-1 font-medium">
                  🔴 Le plus polluant
                </div>
              )}
              {option.saved && (
                <div className="text-green-600 text-sm mt-1">
                  🌱 {option.saved} g économisés ({option.savedPercent}%)
                </div>
              )}
            </button>
          ))}
        </div>

        {/* Frequency & duration */}
        <div className="mb-4">
          <label className="block text-lg font-semibold mb-2">
            Fréquence du trajet :
          </label>
          <select
            className="w-full p-3 border rounded-lg bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
            value={frequency}
            onChange={(e) => setFrequency(e.target.value)}
          >
            <option value="daily">Tous les jours</option>
            <option value="weekly">Chaque semaine</option>
            <option value="monthly">Chaque mois</option>
          </select>
        </div>

        <div className="mb-4">
          <label className="block text-lg font-semibold mb-2">
            Durée de la simulation (en jours) :
          </label>
          <input
            type="number"
            className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
            value={duration}
            onChange={(e) => setDuration(Number(e.target.value))}
            min="1"
            max="365"
          />
        </div>

        <div className="flex justify-center gap-4 mb-6">
          <button
            className="bg-blue-500 text-white px-5 py-3 rounded-lg hover:bg-blue-600 transition disabled:opacity-50"
            onClick={addTrip}
            disabled={loading || !selectedTransport}
          >
            ➕ Ajouter ce trajet
          </button>
        </div>

        {trips.length > 0 && (
          <div className="mt-6 p-4 bg-gray-200 dark:bg-gray-800 rounded-lg">
            <h3 className="text-xl font-semibold mb-2">Trajets simulés</h3>
            <ul>
              {trips.map((trip, index) => (
                <li
                  key={index}
                  className="p-2 border-b flex justify-between items-center border-gray-400 dark:border-gray-600"
                >
                  <div>
                    {trip.start} → {trip.destination} ({trip.name} -{" "}
                    {trip.frequency} - {trip.duration} jours)
                  </div>
                  <button
                    onClick={() => {
                      const updated = [...trips];
                      updated.splice(index, 1);
                      setTrips(updated);
                    }}
                    className="text-red-500 hover:text-red-700 text-sm ml-4"
                    title="Supprimer ce trajet"
                  >
                    🗑️
                  </button>
                </li>
              ))}
            </ul>
            <SimulationChart trips={trips} />
          </div>
        )}

        {trips.length > 0 && token && (
          <div className="flex justify-center mt-4">
            <button
              aria-label="Sauvegarder Simulation"
              className="bg-green-500 text-white px-5 py-3 rounded-lg hover:bg-green-600 transition"
              onClick={handleSaveSimulations}
            >
              ⭐ Sauvegarder Simulation
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Simulation;
