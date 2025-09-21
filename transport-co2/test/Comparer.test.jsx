
import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Compare from "../src/pages/Compare.jsx";
import { vi } from "vitest";
import "@testing-library/jest-dom/vitest";
import { MemoryRouter } from "react-router-dom";

// Mock useLocation
vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useLocation: () => ({
      state: {
        start: "Lyon",
        destination: "Paris"
      }
    }),
  };
});

describe("Compare Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () =>
          Promise.resolve([
            {
              mode: "Voiture Essence",
              co2: 120.5,
              durationMinutes: 90,
              distanceKm: 460,
              label: "Route A7",
              consommationEnergie: 5.6,
              unite: "L",
              prixEstime: 40,
            },
            {
              mode: "Vélo",
              co2: 0,
              durationMinutes: 180,
              distanceKm: 470,
              label: null,
              consommationEnergie: 0,
              unite: "kcal",
              prixEstime: 0,
            },
            {
              mode: "Transport en commun",
              co2: 25,
              durationMinutes: 100,
              distanceKm: 465,
              label: "Bus (30 km) + Métro (20 km)",
              consommationEnergie: 3,
              unite: "kWh",
              prixEstime: 12,
            },
          ]),
      })
    );
  });

  it("renders input fields and search button", () => {
    render(<Compare />, { wrapper: MemoryRouter });
    expect(screen.getByPlaceholderText(/Départ/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Destination/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Rechercher/i })).toBeInTheDocument();
  });

  it("fetches and displays comparison results", async () => {
    render(<Compare />, { wrapper: MemoryRouter });

    fireEvent.click(screen.getByRole("button", { name: /Rechercher/i }));

    await waitFor(() => {
      expect(screen.getByText(/Voiture Essence/i)).toBeInTheDocument();
      expect(screen.getByText(/Vélo/i)).toBeInTheDocument();
      expect(screen.getByText(/Transport en commun/i)).toBeInTheDocument();
    });

    expect(screen.getByText(/Distance estimée :/i)).toBeInTheDocument();
  });

  it("shows loading state during fetch", async () => {
    render(<Compare />, { wrapper: MemoryRouter });
    fireEvent.click(screen.getByRole("button", { name: /Rechercher/i }));
    expect(screen.getByText(/Chargement des données/i)).toBeInTheDocument();
    await screen.findByText(/Voiture Essence/i);
  });

  it("shows fallback message if not enough data", async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([]),
      })
    );

    render(<Compare />, { wrapper: MemoryRouter });
    fireEvent.click(screen.getByRole("button", { name: /Rechercher/i }));

    await waitFor(() => {
      expect(
        screen.getByText(/Lancez une recherche pour afficher les résultats/i)
      ).toBeInTheDocument();
    });
  });
});
