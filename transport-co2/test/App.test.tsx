import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import App from '../src/App.tsx';
import { describe, it, expect } from 'vitest';

describe('App Routing', () => {
  it('renders the Search page on /', () => {
    render(<App />);
    expect(screen.getByRole('heading', { name: /rechercher un trajet/i })).toBeInTheDocument();
  });

  it('renders the Compare page on /comparer', () => {
    window.history.pushState({}, '', '/comparer');
    render(<App />);
    expect(screen.getByRole('heading', { name: /comparer les transports/i })).toBeInTheDocument();
  });

  it('renders the Simulation page on /simulation', () => {
    window.history.pushState({}, '', '/simulation');
    render(<App />);
    expect(screen.getByRole('heading', { name: /simulation d'empreinte carbone/i })).toBeInTheDocument();
  });

  it('renders the Historique page on /historique', () => {
    window.history.pushState({}, '', '/historique');
    render(<App />);
    expect(screen.getByRole('heading', { name: /historique/i })).toBeInTheDocument();
  });

  it('renders the SignUp page on /signup', () => {
    window.history.pushState({}, '', '/signup');
    render(<App />);
    expect(screen.getByRole('heading', { name: /créer un compte/i })).toBeInTheDocument(); // ✅
  });

  it('renders the LogIn page on /login', () => {
    window.history.pushState({}, '', '/login');
    render(<App />);
    expect(screen.getByRole('heading', { name: /connexion/i })).toBeInTheDocument();
  });
  
  

  it('renders the AdminPanel page on /admin', () => {
    window.history.pushState({}, '', '/admin');
    render(<App />);
    expect(screen.getByText(/panneau d'administration/i)).toBeInTheDocument(); // ✅ WORKING
  });
});
