import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import "@testing-library/jest-dom";
import UserManager from "../src/components/UserManager.jsx";
import axios from "axios";
import { vi } from "vitest";

vi.mock("axios");

describe("UserManager Component", () => {
  beforeEach(() => {
    sessionStorage.setItem("token", "fake-token");
    vi.clearAllMocks();
  });

  it("renders user table from API", async () => {
    axios.get.mockResolvedValueOnce({
      data: [
        { id: 1, email: "user1@example.com", nom: "User1", role: "Normal" },
        { id: 2, email: "admin@example.com", nom: "Admin", role: "Admin" },
      ],
    });

    render(<UserManager />);

    expect(screen.getAllByText(/chargement/i)).toHaveLength(2);

    await waitFor(() => {
      expect(screen.getByText(/user1@example.com/i)).toBeInTheDocument();
      expect(screen.getByText(/admin@example.com/i)).toBeInTheDocument();
    });
  });


  it("deletes a user when clicking Supprimer", async () => {
    axios.get.mockResolvedValueOnce({
      data: [
        { id: 1, email: "user1@example.com", nom: "User1", role: "Normal" },
      ],
    });
    axios.delete.mockResolvedValueOnce({});

    render(<UserManager />);

    await screen.findByText(/user1@example.com/i);
    fireEvent.click(screen.getByText(/supprimer/i));

    await waitFor(() => {
      expect(axios.delete).toHaveBeenCalled();
    });
  });

  it("shows error message if API fails", async () => {
    axios.get.mockRejectedValueOnce(new Error("API error"));

    render(<UserManager />);

    await waitFor(() => {
      expect(
        screen.getByText(/impossible de charger les utilisateurs/i)
      ).toBeInTheDocument();
    });
  });
});
