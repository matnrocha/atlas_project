// components/Gauge.js
import React from "react";
import { CircularProgressbar, buildStyles } from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";

function Gauge({ label, value, unit, minValue, maxValue }) {
  // Define colors based on value ranges (definir isto melhor depois)
  const getColor = () => {
    if (value < 10) return "#FF4500"; // red
    if (value < 50) return "#FFA500"; // yellow
    return "#3e98c7"; // blue
  };

  return (
    <div
      style={{
        textAlign: "center",
        padding: "15px",
        color: "#fff",
        backgroundColor: "rgba(255, 255, 255, 0.04)",
        borderRadius: "10px",
        width: "120px",
        margin: "10px auto",
        boxShadow: "0 0 10px rgba(0, 0, 0, 0.2)",
      }}
    >
      <div style={{ width: "90px", margin: "auto", position: "relative" }}>
        <CircularProgressbar
          value={value}
          minValue={minValue || 0} // Dynamically set min value if provided
          maxValue={maxValue || 100} // Dynamically set max value if provided
          text={`${value}`} // Display value in the center
          styles={buildStyles({
            textColor: "#fff",
            pathColor: getColor(),
            trailColor: "#333",
            textSize: "1.2rem",
          })}
        />
      </div>
      <div style={{ fontSize: "0.8rem", color: "#C4C4C4", marginTop: "10px" }}>
        {label.toUpperCase()}
      </div>
      <div style={{ fontSize: "1rem", color: "#C4C4C4" }}>
        {unit}
      </div>
    </div>
  );
}

export default Gauge;
