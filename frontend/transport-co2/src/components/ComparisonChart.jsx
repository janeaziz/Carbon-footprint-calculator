import React, { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
} from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ChartDataLabels
);

const ComparisonChart = ({ selectedOptions }) => {
  const [textColor, setTextColor] = useState("#000");

  useEffect(() => {
    const isDark = document.documentElement.classList.contains("dark");
    setTextColor(isDark ? "#fff" : "#000");
  }, []);

  const labels = [
    "Durée (min)",
    "Émissions CO₂ (g)",
    "Coût estimé (€)",
    "Conso. énergie"
  ];

  const getValue = (val) =>
    val != null && !isNaN(parseFloat(val)) ? parseFloat(val) : 0;

  const durations = selectedOptions.map((opt) => getValue(opt.time));
  const emissions = selectedOptions.map((opt) => getValue(opt.co2));
  const prices = selectedOptions.map((opt) => getValue(opt.prixEstime));
  const consumptions = selectedOptions.map((opt) =>
    getValue(opt.consommationEnergie)
  );

  const transportLabels = selectedOptions.map((opt) => opt.name);

  const datasets = transportLabels.map((label, i) => {
    const color = [
      "rgba(255, 99, 132, 0.6)",
      "rgba(54, 162, 235, 0.6)",
      "rgba(255, 206, 86, 0.6)",
      "rgba(75, 192, 192, 0.6)",
      "rgba(153, 102, 255, 0.6)"
    ][i % 5];

    const border = color.replace("0.6", "1");

    return {
      label,
      data: [durations[i], emissions[i], prices[i], consumptions[i]],
      backgroundColor: color,
      borderColor: border,
      borderWidth: 1
    };
  });

  const data = {
    labels,
    datasets
  };

  const options = {
    responsive: true,
    indexAxis: "y",
    plugins: {
      legend: {
        display: true,
        position: "bottom",
        labels: {
          color: textColor,
          font: { size: 14 }
        }
      },

      datalabels: {
        anchor: "end",
        align: "right",
        color: textColor,
        font: {
          weight: "bold",
          size: 11
        },
        formatter: (val) => val.toFixed(0)
      }
    },
    scales: {
      x: {
        beginAtZero: true,
        ticks: { color: textColor },
        grid: { color: textColor === "#000" ? "rgba(249, 240, 240, 0.1)" : "rgba(244, 232, 232, 0.1)" }
      },
      y: {
        ticks: { color: textColor },
        grid: { color: textColor === "#000" ? "rgba(239, 222, 222, 0.1)" : "rgba(255,255,255,0.1)" }
      }
    }
  };

  return (
    <div className="mt-6 p-4 rounded-lg bg-white dark:bg-gray-900">
      <h3 className="text-xl font-semibold text-center text-black dark:text-white mb-4">
        Comparaison des transports
      </h3>
      <div className="h-auto min-h-[400px]">
        <Bar data={data} options={options} />
      </div>
    </div>
  );
};

export default ComparisonChart;
