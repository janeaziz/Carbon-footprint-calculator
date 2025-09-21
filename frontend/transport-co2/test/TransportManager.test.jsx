import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import "@testing-library/jest-dom";
import TransportManager from "../src/components/TransportManager.jsx";
import { vi } from "vitest";
import axios from "axios";

vi.mock("axios");

describe("TransportManager Component", () => {
  beforeEach(() => {
    sessionStorage.setItem("token", "test-token");
    vi.clearAllMocks();
  });

  it("renders the table with fetched transport data", async () => {
    axios.get.mockResolvedValueOnce({
      data: [
        { id: 1, nom: "Train", consommationMoyenne: 12.5 },
        { id: 2, nom: "Bus", consommationMoyenne: 78 },
      ],
    });

    render(<TransportManager />);

    expect(await screen.findByText(/train/i)).toBeInTheDocument();
    expect(screen.getByText(/12.5 g\/km/i)).toBeInTheDocument();
    expect(screen.getByText(/bus/i)).toBeInTheDocument();
  });

  it("allows adding a new transport", async () => {
    axios.get.mockResolvedValueOnce({ data: [] });
    axios.post.mockResolvedValueOnce({});
    axios.get.mockResolvedValueOnce({ data: [] });

    render(<TransportManager />);

    fireEvent.change(screen.getByPlaceholderText("Nom transport"), {
      target: { value: "Train" },
    });
    fireEvent.change(screen.getByPlaceholderText("CO₂ g/km"), {
      target: { value: "0" },
    });
    fireEvent.click(screen.getByRole("button", { name: /ajouter/i }));

    await waitFor(() => {
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringMatching(/\/api\/admin\/transports$/),
        expect.objectContaining({
          nom: "Train",
          consommationMoyenne: 0,
        }),
        expect.objectContaining({
          headers: expect.objectContaining({
            Authorization: expect.stringContaining("Bearer"),
          }),
        })
      );
    });
  });

  it("allows editing and saving a transport", async () => {
    axios.get.mockResolvedValueOnce({
      data: [{ id: 1, nom: "Train", consommationMoyenne: 14 }],
    });
    axios.put.mockResolvedValueOnce({});
    axios.get.mockResolvedValueOnce({ data: [] });

    render(<TransportManager />);

    await screen.findByText(/train/i);
    fireEvent.click(screen.getByText(/✎/));
    fireEvent.change(screen.getByDisplayValue("Train"), {
      target: { value: "Train Express" },
    });
    fireEvent.change(screen.getByDisplayValue("14"), {
      target: { value: "13" },
    });
    fireEvent.click(screen.getByText("✓"));

    await waitFor(() => {
      expect(axios.put).toHaveBeenCalledWith(
        expect.stringContaining("/api/admin/transports/1"),
        expect.objectContaining({
          nom: "Train Express",
          consommationMoyenne: 13,
        }),
        expect.objectContaining({ headers: expect.any(Object) })
      );
    });
  });

  it("allows deleting a transport", async () => {
    sessionStorage.setItem("token", "fake-token");

    axios.get.mockResolvedValueOnce({
      data: [{ id: 1, nom: "Bus", consommationMoyenne: 80 }],
    });
    axios.delete.mockResolvedValueOnce({});

    render(<TransportManager />);

    await screen.findByText(/bus/i);

    fireEvent.click(screen.getByRole("button", { name: /🗑/i }));

    await waitFor(() => {
      expect(axios.delete).toHaveBeenCalled();
    });
  });
});
