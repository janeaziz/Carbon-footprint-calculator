import React, { useEffect, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { Menu, X, Sun, Moon } from "lucide-react";
import { motion } from "framer-motion";
import UserDropdown from "./UserDropdown";

const Navbar = () => {
  const [user, setUser] = useState(null);
  const [role, setRole] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);
  const [theme, setTheme] = useState("light");
  const [scrolled, setScrolled] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const updateUserFromStorage = () => {
    const storedUser = JSON.parse(sessionStorage.getItem("user"));
    if (storedUser) {
      setUser(storedUser);
      setRole(storedUser.role);
    } else {
      setUser(null);
      setRole("");
    }
  };

  useEffect(() => {
    updateUserFromStorage();
    window.addEventListener("storage", updateUserFromStorage);
    return () => window.removeEventListener("storage", updateUserFromStorage);
  }, []);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 10);
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    const saved = localStorage.getItem("theme") || "light";
    setTheme(saved);
    document.documentElement.classList.toggle("dark", saved === "dark");
  }, []);

  const handleLogout = () => {
    sessionStorage.removeItem("user");
    sessionStorage.removeItem("token");
    setUser(null);
    setRole("");
    window.dispatchEvent(new Event("storage"));
    alert("Déconnexion réussie !");
    navigate("/login");
  };

  const toggleTheme = () => {
    const newTheme = theme === "light" ? "dark" : "light";
    localStorage.setItem("theme", newTheme);
    setTheme(newTheme);
    document.documentElement.classList.toggle("dark", newTheme === "dark");
  };

  const toggleMenu = () => setMenuOpen(!menuOpen);
  const isActive = (path) => location.pathname.startsWith(path);

  return (
    <nav className={`fixed top-0 left-0 right-0 z-50 transition-shadow 
      ${scrolled ? "shadow-md" : ""} 
      bg-blue-600 dark:bg-blue-900 text-white py-3 md:py-4 text-base`}>
      <div className="container max-w-screen-xl mx-auto px-4 flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold tracking-wide hover:opacity-90">
          Eco<span className="text-white font-extrabold">Voyage</span>
        </Link>

        {/* Mobile toggle */}
        <div className="md:hidden">
          <button onClick={toggleMenu} aria-label="Menu">
            {menuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
          </button>
        </div>

        {/* Desktop nav */}
        <div className="hidden md:flex items-center gap-6 font-medium">
          {["/rechercher", "/comparer", "/simulation"].map((path) => (
            <Link
              key={path}
              to={path}
              className={`relative after:absolute after:left-0 after:bottom-0 after:h-[2px] after:bg-white after:transition-all after:duration-300 ${
                isActive(path)
                  ? "after:w-full font-semibold"
                  : "after:w-0 hover:after:w-full"
              }`}
            >
              {path.slice(1).charAt(0).toUpperCase() + path.slice(2)}
            </Link>
          ))}

          {user && (
            <Link
              to="/historique"
              className={`relative after:absolute after:left-0 after:bottom-0 after:h-[2px] after:bg-white after:transition-all after:duration-300 ${
                isActive("/historique")
                  ? "after:w-full font-semibold"
                  : "after:w-0 hover:after:w-full"
              }`}
            >
              Historique
            </Link>
          )}

          {role === "Admin" && (
            <Link
              to="/admin"
              className={`relative after:absolute after:left-0 after:bottom-0 after:h-[2px] after:bg-white after:transition-all after:duration-300 ${
                isActive("/admin")
                  ? "after:w-full font-semibold"
                  : "after:w-0 hover:after:w-full"
              }`}
            >
              ⚙️ Admin Panel
            </Link>
          )}

          {/* Theme switch */}
          <button onClick={toggleTheme} className="hover:opacity-75" aria-label="Toggle Theme">
            {theme === "dark" ? <Moon className="w-5 h-5" /> : <Sun className="w-5 h-5" />}
          </button>

          {/* User/Auth */}
          {user ? (
            <UserDropdown user={user} handleLogout={handleLogout} />
          ) : (
            <div className="flex gap-3">
              <Link to="/login" className="hover:underline">Connexion</Link>
              <Link to="/signup" className="hover:underline">Inscription</Link>
            </div>
          )}
        </div>
      </div>

      {/* Mobile Menu */}
      {menuOpen && (
        <motion.div
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          className="md:hidden flex flex-col gap-3 mt-4 px-6 text-base"
        >
          {["/rechercher", "/comparer", "/simulation"].map((path) => (
            <Link
              key={path}
              to={path}
              onClick={toggleMenu}
              className={`hover:underline ${
                isActive(path) ? "underline font-semibold" : ""
              }`}
            >
              {path.slice(1).charAt(0).toUpperCase() + path.slice(2)}
            </Link>
          ))}
          {user && <Link to="/historique" onClick={toggleMenu}>Historique</Link>}
          {role === "Admin" && <Link to="/admin" onClick={toggleMenu}>⚙️ Admin Panel</Link>}

          <button onClick={toggleTheme}>
            {theme === "dark" ? "🌙 Mode Sombre" : "☀️ Mode Clair"}
          </button>

          {user ? (
            <div className="bg-white/10 p-3 rounded">
              <p className="text-xs font-medium">
                Bienvenue, {user.nom || user.email}
              </p>
              <Link to="/account" onClick={toggleMenu} className="underline text-sm">
                ⚙️ Mon compte
              </Link>
              <button
                onClick={() => {
                  toggleMenu();
                  handleLogout();
                }}
                className="mt-2 bg-red-500 text-sm px-3 py-1 rounded hover:bg-red-600"
              >
                Déconnexion
              </button>
            </div>
          ) : (
            <div className="flex flex-col gap-2">
              <Link to="/login" onClick={toggleMenu}>Connexion</Link>
              <Link to="/signup" onClick={toggleMenu}>Inscription</Link>
            </div>
          )}
        </motion.div>
      )}
    </nav>
  );
};

export default Navbar;
