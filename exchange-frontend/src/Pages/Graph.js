import { React, useState, useEffect } from "react";
import "chart.js/auto";
import { Line } from "react-chartjs-2";
import { Typography } from "@material-ui/core";

var SERVER_URL = "http://127.0.0.1:5000";

function Graph() {
  let [Date, setDate] = useState([]);
  let [buyValue, setBuyValue] = useState([]);
  let [sellValue, setSellValue] = useState([]);

  function graph() {
    fetch(`${SERVER_URL}/graph`)
      .then((response) => response.json())
      .then((data) => {
        setDate(Object.keys(data.buy));
        setBuyValue(Object.values(data.buy));
        setSellValue(Object.values(data.sell));
      });
  }

  useEffect(graph, []);

  return (
    <div style={{ margin: "2em", height: "70vh" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          marginBottom: "1em",
        }}
      >
        <Typography variant="h4" centered gutterBottom>
          Exchange Rate Chart
        </Typography>
      </div>
      <Line
        style={{ height: "100%" }}
        data={{
          labels: Date,
          datasets: [
            {
              label: "Buy",
              data: buyValue,
              backgroundColor: ["rgba(255, 99, 132, 0.2)"],
              borderColor: ["rgba(255, 99, 132, 1)"],
              borderWidth: 1,
            },
            {
              label: "Sell",
              data: sellValue,
              backgroundColor: ["rgba(54, 162, 235, 0.2)"],
              borderColor: ["rgba(54, 162, 235, 1)"],
              borderWidth: 1,
            },
          ],
        }}
        height={400}
        width={600}
        options={{
          cubicInterpolationMode: "monotone",
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: false,
            },
          },
        }}
      />
    </div>
  );
}

export default Graph;
