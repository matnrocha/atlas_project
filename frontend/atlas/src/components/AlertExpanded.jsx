import React from "react";
import { motion } from "framer-motion";

const AlertExpanded = ({ alert, onClose }) => {
  const getSeverityClass = (severity) => {
    switch (severity) {
      case "HIGH":
        return "alert-high";
      case "MEDIUM":
        return "alert-medium";
      case "LOW":
        return "alert-low";
      default:
        return "alert-fixed";
    }
  };

  const getTitle = (shipSystem) => {
    switch (shipSystem) {
      case "LIFE_SUPPORT":
        return "Life Support";
      case "SURROUNDINGS_AND_NAVIGATION":
        return "Surroundings and Navigation";
      case "VENTILATION":
        return "Ventilation";
      case "THERMAL_SYSTEM":
        return "Thermal System";
      case "POWER_SYSTEM":
        return "Power System";
      case "COMMUNICATIONS":
        return "Communications";
      default:
        return "Unknown System";
    }
  };

  return (
    <motion.div
      className={`expanded-alert ${getSeverityClass(alert.priority)}`}
      initial={{ opacity: 0, scale: 0.9, y: 10 }}
      animate={{ opacity: 1, scale: 1, y: 0 }}
      exit={{ opacity: 0, scale: 0.9, y: 10 }}
      transition={{ duration: 0.3 }}
    >
      {/* Close button */}
      <h3 className="alert-title">{getTitle(alert.shipSystem)}</h3>
      <div className="alert-status-bar">
        <div
          className={`status-indicator priority-${alert.priority.toLowerCase()}`}
        />
      </div>
      <div className="alert-details">
        <p>
          <strong style={{ paddingBottom: "10px", color: "white" }}>
            Status:
          </strong>{" "}
          {alert.status}
        </p>
        <p>
          <strong style={{ paddingBottom: "10px", color: "white" }}>
            Date:
          </strong>{" "}
          {new Date(alert.timestamp).toLocaleString()}
        </p>
        <p>
          <strong style={{ paddingBottom: "10px", color: "white" }}>
            Priority:
          </strong>{" "}
          <span className={`priority-${alert.priority.toLowerCase()}`}>
            {alert.priority}
          </span>
        </p>
        <p>
          <strong style={{ paddingBottom: "10px", color: "white" }}>
            Cause:
          </strong>{" "}
          {alert.cause}
        </p>
      </div>
      <button className="close-btn" onClick={onClose}>
        Close
      </button>
    </motion.div>
  );
};

export default AlertExpanded;
