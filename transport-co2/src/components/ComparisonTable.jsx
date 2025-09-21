import React from "react";

const ComparisonTable = ({ selectedOptions }) => {
  return (
    <div className="mt-6 overflow-x-auto">
      <table className="w-full border border-gray-300 dark:border-gray-700 shadow-lg rounded-lg text-black dark:text-white">
        <thead>
          <tr className="bg-blue-500 text-white">
            <th className="p-3">Mode</th>
            <th className="p-3">Temps</th>
            <th className="p-3">Coût</th>
            <th className="p-3">Émissions CO₂</th>
            <th className="p-3">Consommation Énergie</th>
          </tr>
        </thead>
        <tbody>
          {selectedOptions.map((option, index) => (
            <tr
              key={index}
              className="text-center bg-gray-100 dark:bg-gray-900 border-b dark:border-gray-700"
            >
              <td className="p-3">{option.name}</td>
              <td className="p-3">
                {option.time != null && !isNaN(parseFloat(option.time))
                  ? parseFloat(option.time).toFixed(2)
                  : "-"}
              </td>
              <td className="p-3">
                {option.prixEstime != null && !isNaN(parseFloat(option.prixEstime))
                  ? `${parseFloat(option.prixEstime).toFixed(2)} €`
                  : "-"}
              </td>
              <td className="p-3">
                {option.co2 != null && !isNaN(parseFloat(option.co2))
                  ? `${parseFloat(option.co2).toFixed(2)} g/km`
                  : "-"}
              </td>
              <td className="p-3">
                {option.consommationEnergie != null && !isNaN(parseFloat(option.consommationEnergie))
                  ? `${parseFloat(option.consommationEnergie).toFixed(2)} ${option.unite || ""}`
                  : "-"}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ComparisonTable;
