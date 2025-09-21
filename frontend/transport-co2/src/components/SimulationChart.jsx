import React, { useState } from "react";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend
);

const SimulationChart = ({ trips }) => {
  const [visibleTrips, setVisibleTrips] = useState(trips.map(() => true));

  if (!trips || trips.length === 0) {
    return (
      <p className="text-center text-gray-500">
        Aucun trajet enregistré pour la simulation.
      </p>
    );
  }

  const getEmissionsData = (trip) => {
    let emissionsPerDay;
    switch (trip.frequency) {
      case "daily":
        emissionsPerDay = trip.co2;
        break;
      case "weekly":
        emissionsPerDay = trip.co2 / 7;
        break;
      case "monthly":
        emissionsPerDay = trip.co2 / 30;
        break;
      default:
        emissionsPerDay = trip.co2;
    }

    let cumulative = [];
    let total = 0;
    for (let i = 1; i <= trip.duration; i++) {
      total += emissionsPerDay;
      cumulative.push(Number(total.toFixed(2)));
    }
    return cumulative;
  };

  const generateColor = (index) => {
    const hue = (index * 137.508) % 360;
    return `hsl(${hue}, 70%, 45%)`;
  };

  const activeTrips = trips.filter((_, i) => visibleTrips[i]);

  const benchmark = Array.from({ length: trips[0].duration }, (_, i) => {
    const totals = activeTrips.map((trip) => getEmissionsData(trip)[i]);
    const average = totals.reduce((a, b) => a + b, 0) / totals.length;
    return Number(average.toFixed(2));
  });

  const data = {
    labels: Array.from({ length: trips[0].duration }, (_, i) => i + 1),
    datasets: [
      ...trips.map((trip, index) => {
        if (!visibleTrips[index]) return null;
        const color = generateColor(index);
        const isZeroEmission = trip.co2 === 0;
        const isPolluting = trip.co2 > 80000;

        return {
          label: `${trip.name} (${trip.start} → ${trip.destination})`,
          data: getEmissionsData(trip),
          borderColor: color,
          backgroundColor: function (ctx) {
            const gradient = ctx.chart.ctx.createLinearGradient(0, 0, 0, 400);
            gradient.addColorStop(0, color.replace("hsl", "hsla").replace(")", ", 0.4)"));
            gradient.addColorStop(1, color.replace("hsl", "hsla").replace(")", ", 0)"));
            return gradient;
          },
          borderWidth: isZeroEmission ? 3 : 2,
          borderDash: isPolluting ? [8, 4] : undefined,
          fill: true,
          tension: 0.3,
          pointRadius: 2,
          pointHoverRadius: 6,
          pointHoverBorderWidth: 3,
          pointHoverBackgroundColor: "#fff",
        };
      }).filter(Boolean),
      {
        label: "Moyenne des émissions",
        data: benchmark,
        borderColor: "#888",
        borderDash: [5, 5],
        borderWidth: 2,
        pointRadius: 0,
        fill: false,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "bottom",
        labels: {
          font: { size: 12 },
          padding: 10,
        },
      },
      tooltip: {
        callbacks: {
          label: (ctx) => {
            return `${ctx.dataset.label}: ${parseFloat(ctx.raw).toLocaleString()} g CO₂ cumulés`;
          },
        },
      },
    },
    scales: {
      y: {
        title: {
          display: true,
          text: "CO₂ cumulées",
        },
        ticks: {
          callback: (value) => `${value.toLocaleString()} g`,
        },
      },
      x: {
        title: {
          display: true,
          text: "Jour",
        },
      },
    },
  };

  return (
    <div className="mt-6 dark:bg-gray-900 p-4 rounded-lg">
      <h3 className="text-xl font-semibold text-center mb-4">
        Évolution des émissions sur la période
      </h3>

      <div className="flex flex-wrap justify-center gap-4 mb-4">
        {trips.map((trip, index) => (
          <label key={index} className="text-sm flex items-center gap-2">
            <input
              type="checkbox"
              checked={visibleTrips[index]}
              onChange={() => {
                const updated = [...visibleTrips];
                updated[index] = !updated[index];
                setVisibleTrips(updated);
              }}
            />
            {trip.name}
          </label>
        ))}
      </div>

      <div className="h-[400px]">
        <Line data={data} options={options} />
      </div>
    </div>
  );
};

export default SimulationChart;