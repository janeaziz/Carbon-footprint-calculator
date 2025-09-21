import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Navbar from "../src/components/Navbar.jsx";
import { vi } from "vitest";
import "@testing-library/jest-dom/vitest";

// Mock useNavigate
const mockNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe("Navbar Component", () => {
  beforeEach(() => {
    sessionStorage.clear();
    vi.clearAllMocks();
  });

  it("renders basic links when not logged in", () => {
    render(<Navbar />, { wrapper: MemoryRouter });

    expect(
      screen.getByRole("link", { name: /Eco.*Voyage/i })
    ).toBeInTheDocument();

    expect(screen.getByText(/Rechercher/i)).toBeInTheDocument();
    expect(screen.getByText(/Comparer/i)).toBeInTheDocument();
    expect(screen.getByText(/Simulation/i)).toBeInTheDocument();
    expect(screen.getByText(/Connexion/i)).toBeInTheDocument();
    expect(screen.getByText(/Inscription/i)).toBeInTheDocument();
  });

  it("renders user info and Historique when logged in", () => {
    sessionStorage.setItem(
      "user",
      JSON.stringify({ email: "user@example.com", role: "User" })
    );

    render(<Navbar />, { wrapper: MemoryRouter });

    expect(screen.getByText(/user@example\.com/i)).toBeInTheDocument();

    expect(screen.getByText(/Historique/i)).toBeInTheDocument();
    expect(screen.queryByText(/⚙️ Admin Panel/i)).not.toBeInTheDocument();
  });

  it("renders admin panel link when role is Admin", () => {
    sessionStorage.setItem(
      "user",
      JSON.stringify({ email: "admin@example.com", role: "Admin" })
    );

    render(<Navbar />, { wrapper: MemoryRouter });

    expect(screen.getByText(/⚙️ Admin Panel/i)).toBeInTheDocument();
  });

  it("logs out and clears sessionStorage on logout click", async () => {
    global.alert = vi.fn();
    sessionStorage.setItem(
      "user",
      JSON.stringify({ email: "logout@example.com", role: "User" })
    );
    sessionStorage.setItem("token", "mock-token");

    // Simule écran mobile pour que le bouton Déconnexion s'affiche
    window.innerWidth = 400;
    window.dispatchEvent(new Event("resize"));

    render(<Navbar />, { wrapper: MemoryRouter });

    // Ouvre le menu mobile
    fireEvent.click(screen.getByLabelText(/menu/i));

    // Clique sur Déconnexion
    const logoutBtn = await screen.findByText((text) =>
      text.toLowerCase().includes("déconnexion")
    );
    fireEvent.click(logoutBtn);

    expect(sessionStorage.getItem("user")).toBe(null);
    expect(sessionStorage.getItem("token")).toBe(null);
    expect(global.alert).toHaveBeenCalledWith("Déconnexion réussie !");
    expect(mockNavigate).toHaveBeenCalledWith("/login");
  });
});
