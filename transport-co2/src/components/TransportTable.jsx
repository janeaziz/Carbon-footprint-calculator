import React from 'react';

const TransportTable = ({ transportOptions }) => {
  return (
    <div className="mt-6 overflow-x-auto">
      <table className="w-full border border-gray-300 dark:border-gray-700 shadow-lg rounded-lg text-black dark:text-white">
        <thead>
          <tr className="bg-blue-500 text-white">
            <th className="p-3">Mode</th>
            <th className="p-3">Temps</th>
            <th className="p-3">Coût (€)</th>
            <th className="p-3">Émissions CO₂ (g/km)</th>
          </tr>
        </thead>
        <tbody>
          {transportOptions.map((option) => (
            <tr key={option.id} className="text-center bg-gray-100 dark:bg-gray-800">
              <td className="p-3">{option.name}</td>
              <td className="p-3">{option.time}</td>
              <td className="p-3">{option.cost}€</td>
              <td className="p-3">{option.co2} g/km</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TransportTable;
