import React from 'react';

const transportOptions = [
  { id: 1, name: "Avion", time: "1h20", cost: "120€", co2: "285g CO₂/km", icon: "✈️" },
  { id: 2, name: "Voiture Essence", time: "3h00", cost: "50€", co2: "210g CO₂/km", icon: "🚗" },
  { id: 3, name: "Train", time: "2h45", cost: "35€", co2: "14g CO₂/km", icon: "🚆" },
  { id: 4, name: "Vélo", time: "6h30", cost: "0€", co2: "0g CO₂/km", icon: "🚲" },
];

const TransportList = () => {
  return (
    <div className="mt-6 max-w-3xl mx-auto">
      <table className="w-full border border-gray-300 dark:border-gray-700 shadow-lg rounded-lg overflow-hidden text-lg text-black dark:text-white">
        <thead>
          <tr className="bg-blue-500 text-white">
            <th className="border border-gray-300 p-4">Mode</th>
            <th className="border border-gray-300 p-4">Temps</th>
            <th className="border border-gray-300 p-4">Coût</th>
            <th className="border border-gray-300 p-4">Émissions</th>
          </tr>
        </thead>
        <tbody>
          {transportOptions.map((option, index) => (
            <tr
              key={option.id}
              className={`text-center ${
                index % 2 === 0
                  ? "bg-gray-100 dark:bg-gray-800"
                  : "bg-white dark:bg-gray-900"
              }`}
            >
              <td className="border border-gray-300 dark:border-gray-600 p-4 flex items-center justify-center">
                <span className="mr-2 text-xl">{option.icon}</span> {option.name}
              </td>
              <td className="border border-gray-300 dark:border-gray-600 p-4">{option.time}</td>
              <td className="border border-gray-300 dark:border-gray-600 p-4">{option.cost}</td>
              <td className="border border-gray-300 dark:border-gray-600 p-4">{option.co2}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TransportList;
