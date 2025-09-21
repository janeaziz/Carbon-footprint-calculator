import React from 'react';
import TransportManager from "../components/TransportManager.jsx";
import UserManager from "../components/UserManager.jsx";

const AdminPanel = () => {
  return (
    <div className="min-h-screen bg-gray-100 dark:bg-gray-950 text-black dark:text-white transition-colors">
      <div className="max-w-6xl mx-auto mt-10 p-6 bg-white dark:bg-gray-800 shadow-lg rounded-lg border border-gray-300 dark:border-gray-700">
        <h2 className="text-3xl font-bold text-center mb-6">
          Panneau d'administration
        </h2>

        {/* Section for managing transports */}
        <TransportManager />

        {/* Section for managing users */}
        <UserManager />
      </div>
    </div>
  );
};

export default AdminPanel;
