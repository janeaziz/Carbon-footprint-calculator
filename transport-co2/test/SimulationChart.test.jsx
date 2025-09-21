import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import SimulationChart from "../src/components/SimulationChart.jsx";

describe("SimulationChart Component", () => {
  it("shows fallback message if no trips provided", () => {
    render(<SimulationChart trips={[]} />);
    expect(
      screen.getByText(/aucun trajet enregistré pour la simulation/i)
    ).toBeInTheDocument();
  });

  it("renders chart with correct title and labels", () => {
    const mockTrips = [
      {
        name: "Train",
        start: "Paris",
        destination: "Lyon",
        co2: 14,
        frequency: "daily",
        duration: 3,
      },
    ];

    render(<SimulationChart trips={mockTrips} />);

    // ✅ Chart title
    expect(
      screen.getByText(/évolution des émissions sur la période/i)
    ).toBeInTheDocument();

    // ✅ Canvas from Chart.js
    expect(screen.getByRole("img")).toBeInTheDocument(); // Chart.js renders `<canvas role="img">`
  });
});
