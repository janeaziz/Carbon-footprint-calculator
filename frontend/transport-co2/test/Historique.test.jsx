// test/Historique.test.jsx
import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import Historique from "../src/pages/Historique.jsx";
import { vi } from "vitest";
import axios from "axios";
import { MemoryRouter } from "react-router-dom";

vi.mock("axios");

describe("Historique Component", () => {
  beforeEach(() => {
    sessionStorage.clear();
    vi.clearAllMocks();
  });

  it("renders empty state if no token is present", () => {
    render(<Historique />, { wrapper: MemoryRouter });
    expect(screen.getByText(/aucun trajet sauvegardé/i)).toBeInTheDocument();
  });

  it("fetches and displays historical data when token is present", async () => {
    sessionStorage.setItem("token", "mock-token");
    const mockData = [
      {
        id: 1,
        origine: "Paris",
        destination: "Lyon",
        date: "2024-04-10T12:00:00Z",
        modes: [
          { mode: "Train", co2: 14.5 },
          { mode: "Avion", subMode: "Long-courrier", co2: 285 },
        ],
      },
    ];

    axios.get.mockResolvedValueOnce({ data: mockData });

    render(<Historique />, { wrapper: MemoryRouter });

    await waitFor(() => {
      expect(screen.getByText(/paris → lyon/i)).toBeInTheDocument();
    });
  });

  it("removes a trajet on delete click", async () => {
    sessionStorage.setItem("token", "mock-token");
    vi.spyOn(window, "confirm").mockReturnValue(true);

    const mockData = [
      {
        id: 1,
        origine: "Nice",
        destination: "Marseille",
        date: "2024-04-10T12:00:00Z",
        modes: [],
      },
    ];

    axios.get.mockResolvedValueOnce({ data: mockData });
    axios.delete.mockResolvedValueOnce({});

    render(<Historique />, { wrapper: MemoryRouter });

    await waitFor(() => {
      expect(screen.getByText(/nice → marseille/i)).toBeInTheDocument();
    });

    fireEvent.click(screen.getByText(/supprimer/i));

    await waitFor(() => {
      expect(screen.queryByText(/nice → marseille/i)).not.toBeInTheDocument();
    });
  });
});
