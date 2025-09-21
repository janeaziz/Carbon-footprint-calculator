// Polyfill TextEncoder/TextDecoder for jsdom
import { TextEncoder, TextDecoder } from 'util';
import React from 'react';
import '@testing-library/jest-dom';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Login from '../src/pages/Login.jsx';

import { vi, describe, beforeEach, test, expect } from 'vitest';

global.alert = vi.fn();
vi.spyOn(console, 'error').mockImplementation(() => {});

global.TextEncoder = TextEncoder;
global.TextDecoder = TextDecoder;

// ✅ Mock useNavigate from react-router-dom
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

// ✅ Mock the config file to provide a fake API URL
vi.mock('../src/config', () => ({
  API_URL: 'https://mock-api.com',
}));

describe('Login Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  test('renders login form correctly', () => {
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );

    expect(screen.getByPlaceholderText(/email/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/mot de passe/i)).toBeInTheDocument();
    expect(screen.getByText(/se connecter/i)).toBeInTheDocument();
  });

  test('handles successful login', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () =>
          Promise.resolve({
            user: { id: 1, name: 'Test User' },
            token: 'mock-token',
          }),
      })
    );

    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/email/i), {
      target: { value: 'test@example.com' },
    });
    fireEvent.change(screen.getByPlaceholderText(/mot de passe/i), {
      target: { value: 'password123' },
    });

    fireEvent.click(screen.getByText(/se connecter/i));

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/');
    });

    expect(sessionStorage.getItem('user')).toContain('Test User');
    expect(sessionStorage.getItem('token')).toBe('mock-token');

    global.fetch.mockClear();
  });

  test('handles failed login', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: false, // Simulate failed response
        status: 401,
      })
    );

    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/email/i), {
      target: { value: 'wrong@example.com' },
    });
    fireEvent.change(screen.getByPlaceholderText(/mot de passe/i), {
      target: { value: 'wrongpassword' },
    });

    fireEvent.click(screen.getByText(/se connecter/i));

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith("❌ Identifiants incorrects !");
    });

    // Optional: make sure navigate wasn't called
    expect(mockNavigate).not.toHaveBeenCalled();

    global.fetch.mockClear();
  });
});
