import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Simulation from "../src/pages/Simulation.jsx";
import "@testing-library/jest-dom/vitest";
import { vi } from "vitest";

// Mock scrollIntoView for test
beforeAll(() => {
  window.HTMLElement.prototype.scrollIntoView = vi.fn();
});

beforeEach(() => {
  global.alert = vi.fn();
});

// Mock Chart.js
vi.mock("react-chartjs-2", () => ({
  Line: () => <div data-testid="chart-placeholder">Chart</div>,
}));

describe("Simulation Component", () => {
  beforeEach(() => {
    sessionStorage.setItem("token", "test-token");
    global.alert = vi.fn();
    global.fetch = vi
      .fn()
      // GET /transports/search
      .mockResolvedValueOnce({
        ok: true,
        json: () =>
          Promise.resolve([
            {
              mode: "Train",
              co2: 42,
              distance: 100,
              label: "SNCF",
              subMode: "TGV",
            },
            {
              mode: "Voiture",
              co2: 100,
              distance: 100,
              label: "",
              subMode: null,
            },
          ]),
      })
      // POST /trajets
      .mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ id: 1 }),
      })
      // POST /history
      .mockResolvedValueOnce({ ok: true });
  });

  it("renders input fields and add button", () => {
    render(<Simulation />);
    expect(screen.getByPlaceholderText(/départ/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/destination/i)).toBeInTheDocument();
    expect(screen.getByText(/ajouter ce trajet/i)).toBeInTheDocument();
  });

  

  it("fetches and displays transport options", async () => {
    render(<Simulation />);
    fireEvent.change(screen.getByPlaceholderText(/départ/i), {
      target: { value: "Lyon" },
    });
    fireEvent.change(screen.getByPlaceholderText(/destination/i), {
      target: { value: "Paris" },
    });
    fireEvent.click(screen.getByText(/trouver transport/i));
    expect(await screen.findByText(/TGV 100.00 km/)).toBeInTheDocument();
  });

  it("shows badges for eco and worst options", async () => {
    render(<Simulation />);
    fireEvent.change(screen.getByPlaceholderText(/départ/i), {
      target: { value: "Lyon" },
    });
    fireEvent.change(screen.getByPlaceholderText(/destination/i), {
      target: { value: "Paris" },
    });
    fireEvent.click(screen.getByText(/trouver transport/i));
    await screen.findByText(/TGV 100.00 km/);

    expect(
      screen.getByText(/🌿 Meilleur choix écologique/i)
    ).toBeInTheDocument();
    expect(screen.getByText(/🔴 Le plus polluant/i)).toBeInTheDocument();
  });

  
});
