import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Search from "./pages/Search.jsx";
import Compare from "./pages/Compare.jsx";
import Simulation from "./pages/Simulation.jsx";
import Historique from "./pages/Historique.jsx";
import SignUp from "./pages/SignUp.jsx";
import Login from "./pages/LogIn.jsx";
import AdminPanel from "./pages/AdminPanel.jsx";
import Navbar from "./components/Navbar.jsx";
import AccountSettings from "./pages/AccountSettings.jsx";
import Footer from "./components/Footer.jsx";

import "./App.css";

function App() {
  return (
    <div className="min-h-screen bg-white text-black dark:bg-gray-950 dark:text-white transition-colors duration-300">
      <Router>
        <Navbar />
        <main className="pt-20 pb-12">
          <Routes>
            <Route path="/" element={<Search />} />
            <Route path="/rechercher" element={<Search />} />
            <Route path="/comparer" element={<Compare />} />
            <Route path="/simulation" element={<Simulation />} />
            <Route path="/historique" element={<Historique />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/login" element={<Login />} />
            <Route path="/admin" element={<AdminPanel />} />
            <Route path="/account" element={<AccountSettings />} />
          </Routes>
        </main>
        <Footer />
      </Router>
    </div>
  );
}

export default App;
