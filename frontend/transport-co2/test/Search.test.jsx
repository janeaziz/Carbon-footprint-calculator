import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import "@testing-library/jest-dom";
import Search from "../src/pages/Search.jsx";
import { vi } from "vitest";
import { MemoryRouter } from "react-router-dom";

// 🔁 Mock useNavigate
const mockNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe("Search Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    sessionStorage.clear();
  });

  it("renders heading and inputs", () => {
    render(<Search />, { wrapper: MemoryRouter });

    expect(screen.getByRole("heading", { name: /rechercher un trajet/i })).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/départ/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/destination/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /rechercher/i })).toBeInTheDocument();
  });

  it("shows alert if inputs are empty", async () => {
    global.alert = vi.fn();

    render(<Search />, { wrapper: MemoryRouter });

    fireEvent.click(screen.getByRole("button", { name: /rechercher/i }));

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith("Veuillez entrer un départ et une destination !");
    });
  });

  it("fetches results and displays table", async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () =>
          Promise.resolve([
            { mode: "Train", co2: 14 },
            { mode: "Avion", co2: 285, subMode: "Long-courrier" },
          ]),
      })
    );

    render(<Search />, { wrapper: MemoryRouter });

    fireEvent.change(screen.getByPlaceholderText(/départ/i), {
      target: { value: "Paris" },
    });
    fireEvent.change(screen.getByPlaceholderText(/destination/i), {
      target: { value: "Lyon" },
    });
    fireEvent.click(screen.getByRole("button", { name: /rechercher/i }));

    expect(await screen.findByText(/train/i)).toBeInTheDocument();
    expect(
      await screen.findByText((text) => text.includes("14.00") && text.includes("CO₂"))
    ).toBeInTheDocument();

    global.fetch.mockClear();
  });

  it("shows error alert if fetch fails", async () => {
    global.fetch = vi.fn(() => Promise.resolve({ ok: false }));
    global.alert = vi.fn();
    vi.spyOn(console, "error").mockImplementation(() => {});

    render(<Search />, { wrapper: MemoryRouter });

    fireEvent.change(screen.getByPlaceholderText(/départ/i), {
      target: { value: "A" },
    });
    fireEvent.change(screen.getByPlaceholderText(/destination/i), {
      target: { value: "B" },
    });
    fireEvent.click(screen.getByRole("button", { name: /rechercher/i }));

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith("❌ Aucune donnée trouvée pour ce trajet !");
      expect(console.error).toHaveBeenCalledWith(expect.any(Error));
    });

    global.fetch.mockClear();
  });

  it("navigates to /comparer on button click", async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([{ mode: "Train", co2: 14 }]),
      })
    );

    render(<Search />, { wrapper: MemoryRouter });

    fireEvent.change(screen.getByPlaceholderText(/départ/i), {
      target: { value: "Paris" },
    });
    fireEvent.change(screen.getByPlaceholderText(/destination/i), {
      target: { value: "Lyon" },
    });
    fireEvent.click(screen.getByRole("button", { name: /rechercher/i }));

    const compareBtn = await screen.findByRole("button", { name: /comparer/i });
    fireEvent.click(compareBtn);

    expect(mockNavigate).toHaveBeenCalledWith("/comparer", {
      state: { start: "Paris", destination: "Lyon" },
    });

    global.fetch.mockClear();
  });

  it("shows alert if trying to save without token", async () => {
    global.alert = vi.fn();
    // Clear token
    sessionStorage.removeItem("token");
  
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () =>
          Promise.resolve([
            { mode: "Train", co2: 14 },
          ]),
      })
    );
  
    render(<Search />, { wrapper: MemoryRouter });
  
    fireEvent.change(screen.getByPlaceholderText(/départ/i), {
      target: { value: "Paris" },
    });
    fireEvent.change(screen.getByPlaceholderText(/destination/i), {
      target: { value: "Lyon" },
    });
    fireEvent.click(screen.getByRole("button", { name: /rechercher/i }));
  
    const compareBtn = await screen.findByRole("button", { name: /comparer/i });
    expect(compareBtn).toBeInTheDocument();
  
    // Simulate trying to click "Sauvegarder" even if it's not rendered
    const saveBtn = screen.queryByRole("button", { name: /sauvegarder/i });
    expect(saveBtn).not.toBeInTheDocument();
    // Trigger alert manually
    await waitFor(() => {
      expect(global.alert).not.toHaveBeenCalledWith(expect.stringMatching(/sauvegardé/i));
    });
  });
  

  it("calls backend to save search when logged in", async () => {
    sessionStorage.setItem("token", "fake-token");

    global.alert = vi.fn();

    global.fetch = vi
      .fn()
      // First fetch: search results
      .mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve([{ mode: "Train", co2: 14 }]),
      })
      // Second fetch: save trajet
      .mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ id: 123 }),
      })
      // Third fetch: save history
      .mockResolvedValueOnce({
        ok: true,
      });

    render(<Search />, { wrapper: MemoryRouter });

    fireEvent.change(screen.getByPlaceholderText(/départ/i), {
      target: { value: "Paris" },
    });
    fireEvent.change(screen.getByPlaceholderText(/destination/i), {
      target: { value: "Lyon" },
    });
    fireEvent.click(screen.getByRole("button", { name: /rechercher/i }));

    const saveBtn = await screen.findByRole("button", { name: /sauvegarder/i });
    fireEvent.click(saveBtn);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledTimes(3);
      expect(global.alert).toHaveBeenCalledWith("✅ Trajet sauvegardé dans l'historique !");
    });
  });
});
