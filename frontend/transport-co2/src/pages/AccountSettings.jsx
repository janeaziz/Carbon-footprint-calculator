import React, { useState, useEffect } from "react";

const AccountSettings = () => {
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const token = sessionStorage.getItem("token");

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/users/me`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erreur lors de la récupération");
        return res.json();
      })
      .then((data) => setName(data.nom))
      .catch((err) => {
        console.error(err);
        setMessage("❌ Impossible de charger les informations utilisateur.");
      });
  }, [token]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/users/me`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name: name.trim() === "" ? null : name,
          password: password.trim() === "" ? null : password,
        }),
      });

      if (!res.ok) throw new Error(await res.text());

      const updatedUser = await res.json();
      const storedUser = JSON.parse(sessionStorage.getItem("user"));

      sessionStorage.setItem(
        "user",
        JSON.stringify({ ...storedUser, nom: updatedUser.nom })
      );
      window.dispatchEvent(new Event("storage"));
      setMessage("✅ Informations mises à jour avec succès !");
      setPassword("");
    } catch (err) {
      console.error(err);
      setMessage("❌ Erreur lors de la mise à jour : " + err.message);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 dark:bg-gray-950 text-black dark:text-white transition-colors py-10">
      <div className="max-w-xl mx-auto bg-white dark:bg-gray-800 shadow-md rounded-lg p-6 border border-gray-300 dark:border-gray-700">
        <h2 className="text-2xl font-bold mb-6 text-center">Mon Compte</h2>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block font-medium mb-1">Nom</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border border-gray-300 dark:border-gray-600 px-4 py-2 rounded bg-white dark:bg-gray-900 text-black dark:text-white"
              placeholder="Votre nom"
              required
            />
          </div>

          <div>
            <label className="block font-medium mb-1">Nouveau mot de passe</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full border border-gray-300 dark:border-gray-600 px-4 py-2 rounded bg-white dark:bg-gray-900 text-black dark:text-white"
              placeholder="Laissez vide si vous ne changez pas"
            />
          </div>

          <button
            type="submit"
            className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-2 rounded transition"
          >
            Mettre à jour
          </button>
        </form>

        {message && (
          <p className="mt-4 text-center text-sm text-gray-700 dark:text-gray-300">
            {message}
          </p>
        )}
      </div>
    </div>
  );
};

export default AccountSettings;
