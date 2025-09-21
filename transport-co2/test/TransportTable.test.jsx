import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import TransportTable from "../src/components/TransportTable.jsx";

describe("TransportTable Component", () => {
  const mockData = [
    { id: 1, name: "Train", time: "2h45", cost: 35, co2: 14 },
    { id: 2, name: "Avion", time: "1h20", cost: 120, co2: 285 },
  ];

  it("renders headers and all transport rows", () => {
    render(<TransportTable transportOptions={mockData} />);

    // ✅ Headers
    expect(screen.getByText(/mode/i)).toBeInTheDocument();
    expect(screen.getByText(/temps/i)).toBeInTheDocument();
    expect(screen.getByText(/coût/i)).toBeInTheDocument();
    expect(screen.getByText(/émissions CO₂/i)).toBeInTheDocument();

    // ✅ Rows by mode
    expect(screen.getByText(/train/i)).toBeInTheDocument();
    expect(screen.getByText(/avion/i)).toBeInTheDocument();

    // ✅ Times
    expect(screen.getByText(/2h45/i)).toBeInTheDocument();
    expect(screen.getByText(/1h20/i)).toBeInTheDocument();

    // ✅ Cost values with €
    expect(screen.getByText((text) => text.includes("35€"))).toBeInTheDocument();
    expect(screen.getByText((text) => text.includes("120€"))).toBeInTheDocument();

    // ✅ CO₂ values — account for possible formatting like 14 → 14.0
    expect(
      screen.getByText((text) => text.includes("14") && text.includes("g/km"))
    ).toBeInTheDocument();

    expect(
      screen.getByText((text) => text.includes("285") && text.includes("g/km"))
    ).toBeInTheDocument();
  });
});
