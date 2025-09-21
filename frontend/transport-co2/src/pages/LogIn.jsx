import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_URL } from "../config.js";

const Login = () => {
  const [email, setEmail] = useState("");
  const [motDePasse, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, motDePasse }),
      });

      if (!response.ok) throw new Error("Erreur de connexion");

      const data = await response.json();

      sessionStorage.setItem("user", JSON.stringify(data.user));
      sessionStorage.setItem("token", data.token);

      alert("✅ Connexion réussie !");
      window.dispatchEvent(new Event("storage"));
      navigate("/");
    } catch (err) {
      console.error("Login error:", err);
      alert("❌ Identifiants incorrects !");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-50 to-white dark:from-gray-900 dark:to-gray-950 px-4">
      <div className="bg-green-50 dark:bg-gray-800 p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border border-gray-200 dark:border-gray-700 w-full max-w-md text-center">
        
        {/* Illustration */}
        <div className="mb-6">
          <img
            src="/no-search.svg"
            alt="Illustration de connexion"
            className="w-24 h-24 mx-auto dark:hidden"
          />
          <img
            src="/no-search-dark.svg"
            alt="Illustration de connexion"
            className="w-24 h-24 mx-auto hidden dark:block"
          />
        </div>

        <h2 className="text-2xl font-bold mb-4 text-gray-800 dark:text-white">
          Connexion
        </h2>

        <form onSubmit={handleLogin} className="text-left space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Email
            </label>
            <input
              type="email"
              placeholder="Email"
              className="w-full p-2 border rounded bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Mot de passe
            </label>
            <input
              type="password"
              placeholder="Mot de passe"
              className="w-full p-2 border rounded bg-white dark:bg-gray-900 text-black dark:text-white border-gray-300 dark:border-gray-600"
              value={motDePasse}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 rounded transition transform hover:scale-[1.01]"
          >
            Se connecter
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
