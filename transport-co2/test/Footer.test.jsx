// tests/Footer.test.jsx

import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Footer from "../src/components/Footer.jsx";
import "@testing-library/jest-dom/vitest";

describe("Footer Component", () => {
  beforeEach(() => {
    render(<Footer />, { wrapper: MemoryRouter });
  });

  it("renders the EcoVoyage title", () => {
    const heading = screen.getByRole("heading", {
      name: "EcoVoyage",
      level: 4,
    });
    expect(heading).toBeInTheDocument();
  });

  it("displays the slogan", () => {
    expect(
      screen.getByText(/Votre assistant pour des trajets plus responsables 🌱/i)
    ).toBeInTheDocument();
  });

  it("displays navigation links", () => {
    expect(screen.getByText("Rechercher")).toBeInTheDocument();
    expect(screen.getByText("Comparer")).toBeInTheDocument();
    expect(screen.getByText("Simulation")).toBeInTheDocument();
    expect(screen.getByText("Historique")).toBeInTheDocument();
  });

  it("displays contact info", () => {
    expect(screen.getByText(/support@ecovoyage.com/i)).toBeInTheDocument();
    expect(screen.getByText(/Université Lyon 1/i)).toBeInTheDocument();
  });

  it("shows the current year dynamically", () => {
    const year = new Date().getFullYear().toString();
    expect(
      screen.getByText(new RegExp(`© ${year} EcoVoyage`, "i"))
    ).toBeInTheDocument();
  });
});
