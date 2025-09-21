import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import TransportList from "../src/components/TransportList.jsx";

describe("TransportList Component", () => {
  it("renders table with transport options", () => {
    render(<TransportList />);

    // ✅ Check table headers
    expect(screen.getByText(/mode/i)).toBeInTheDocument();
    expect(screen.getByText(/temps/i)).toBeInTheDocument();
    expect(screen.getByText(/coût/i)).toBeInTheDocument();
    expect(screen.getByText(/émissions/i)).toBeInTheDocument();

    // ✅ Check all transport names
    expect(screen.getByText(/avion/i)).toBeInTheDocument();
    expect(screen.getByText(/voiture essence/i)).toBeInTheDocument();
    expect(screen.getByText(/train/i)).toBeInTheDocument();
    expect(screen.getByText(/vélo/i)).toBeInTheDocument();

    // ✅ Flexible matchers for CO₂ values
    expect(
      screen.getByText((text) => text.includes("285") && text.includes("CO₂"))
    ).toBeInTheDocument();

    expect(
      screen.getByText((text) => text.includes("14") && text.includes("CO₂"))
    ).toBeInTheDocument();

    expect(
      screen.getByText((text) => text.trim().match(/^0\s?g\s?CO₂\/?km$/i))
    ).toBeInTheDocument();
  });
});
