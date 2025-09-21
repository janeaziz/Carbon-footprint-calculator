/// <reference types="vitest" />
import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    globals: true,
    environment: 'jsdom',
    include: ['test/**/*.test.{ts,tsx,js,jsx}'],
    coverage: {
      enabled: true,
      reporter: ['lcov', 'text', 'json'],
      reportsDirectory: './coverage',
    },
  },
  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.jsx'], 
  },
});
