import React from "react";
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <footer className="bg-blue-600 dark:bg-blue-900 text-white py-4">
      <div className="max-w-screen-xl mx-auto px-4 grid grid-cols-1 sm:grid-cols-3 gap-6">
        {/* Left */}
        <div>
          <h4 className="font-bold mb-1">EcoVoyage</h4>
          <p className="leading-snug text-sm">
            Votre assistant pour des trajets plus responsables 🌱
          </p>
        </div>

        {/* Middle */}
        <div>
          <h4 className="font-semibold mb-2">Navigation</h4>
          <ul className="space-y-1">
            <li><Link to="/rechercher" className="hover:underline">Rechercher</Link></li>
            <li><Link to="/comparer" className="hover:underline">Comparer</Link></li>
            <li><Link to="/simulation" className="hover:underline">Simulation</Link></li>
            <li><Link to="/historique" className="hover:underline">Historique</Link></li>
          </ul>
        </div>

        {/* Right */}
        <div className="flex flex-col justify-between text-sm">
          <div>
            <h4 className="font-semibold mb-2">Contact</h4>
            <p>Email : <a href="mailto:support@ecovoyage.com" className="hover:underline">support@ecovoyage.com</a></p>
            <p>Université Lyon 1</p>
          </div>
          <p className="text-xs text-gray-200 dark:text-gray-400">
            © {new Date().getFullYear()} EcoVoyage. Tous droits réservés.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
