import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import AccountSettings from '../src/pages/AccountSettings.jsx';
import '@testing-library/jest-dom';

import { vi, describe, test, beforeEach, expect } from 'vitest';

global.alert = vi.fn();
vi.spyOn(console, 'error').mockImplementation(() => {});

import { TextEncoder, TextDecoder } from 'util';
global.TextEncoder = TextEncoder;
global.TextDecoder = TextDecoder;

beforeEach(() => {
  vi.clearAllMocks();
  sessionStorage.clear();
  sessionStorage.setItem('token', 'mock-token');
  sessionStorage.setItem('user', JSON.stringify({ nom: 'Test User', email: 'test@example.com' }));
});

describe('AccountSettings Component', () => {
  test('renders form with inputs', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({ nom: 'Test User' }),
      })
    );

    render(
      <MemoryRouter>
        <AccountSettings />
      </MemoryRouter>
    );

    await screen.findByDisplayValue('Test User');

    expect(screen.getByPlaceholderText(/votre nom/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/laissez vide/i)).toBeInTheDocument();
    expect(screen.getByText(/mettre à jour/i)).toBeInTheDocument();
  });

  test('handles successful update', async () => {
    global.fetch = vi.fn()
      .mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ nom: 'Test User' }),
      })
      .mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ nom: 'Updated User' }),
      });

    render(
      <MemoryRouter>
        <AccountSettings />
      </MemoryRouter>
    );

    await screen.findByDisplayValue('Test User');

    fireEvent.change(screen.getByPlaceholderText(/votre nom/i), {
      target: { value: 'Updated User' },
    });
    fireEvent.click(screen.getByText(/mettre à jour/i));

    await waitFor(() => {
      expect(screen.getByText(/informations mises à jour/i)).toBeInTheDocument();
    });

    const storedUser = JSON.parse(sessionStorage.getItem('user'));
    expect(storedUser.nom).toBe('Updated User');
  });

  test('shows error if update fails', async () => {
    global.fetch = vi.fn()
      .mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve({ nom: 'Test User' }),
      })
      .mockResolvedValueOnce({
        ok: false,
        text: () => Promise.resolve("Update failed"),
      });

    render(
      <MemoryRouter>
        <AccountSettings />
      </MemoryRouter>
    );

    await screen.findByDisplayValue('Test User');

    fireEvent.change(screen.getByPlaceholderText(/votre nom/i), {
      target: { value: 'Failing Update' },
    });
    fireEvent.click(screen.getByText(/mettre à jour/i));

    await waitFor(() => {
      expect(screen.getByText(/erreur lors de la mise à jour/i)).toBeInTheDocument();
    });
  });
});
