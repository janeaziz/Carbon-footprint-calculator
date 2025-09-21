import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";

const UserDropdown = ({ user, handleLogout }) => {
  const [showDropdown, setShowDropdown] = useState(false);
  const timeoutRef = useRef(null);

  const handleEnter = () => {
    clearTimeout(timeoutRef.current);
    setShowDropdown(true);
  };

  const handleLeave = () => {
    timeoutRef.current = setTimeout(() => setShowDropdown(false), 200);
  };

  return (
    <div
      className="relative"
      onMouseEnter={handleEnter}
      onMouseLeave={handleLeave}
    >
      <div className="flex items-center gap-2 cursor-pointer">
        <div className="w-8 h-8 bg-white text-blue-600 dark:bg-gray-300 rounded-full flex items-center justify-center font-bold">
          {user.nom?.[0]?.toUpperCase() || "?"}
        </div>
        <span className="text-sm text-black dark:text-white">{user.nom || user.email}</span>
      </div>

      <AnimatePresence>
        {showDropdown && (
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.2 }}
            className="absolute right-0 mt-2 bg-white dark:bg-gray-800 text-black dark:text-white rounded shadow w-44 z-10 py-2 border border-gray-200 dark:border-gray-600"
          >
            <Link
              to="/account"
              className="flex items-center gap-2 px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-700 text-sm"
            >
              ⚙️ Mon compte
            </Link>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 w-full text-left px-4 py-2 hover:bg-red-100 dark:hover:bg-red-900 text-sm"
            >
              🔒 Déconnexion
            </button>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default UserDropdown;
