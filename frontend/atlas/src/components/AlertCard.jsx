import React, { useContext } from "react";

const AlertCard = ({ alert , onExpand}) => {
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
            return "";
    }
  };


return (
    <div className={`alert-card ${getSeverityClass(alert.priority)}`}>
      <div className="alert-title">
      <h3>{getTitle(alert.shipSystem)}</h3>
        <p>{alert.status}</p>
      </div>
      <p>{new Date(alert.timestamp).toLocaleString()}</p>
      {/* <p>
        <strong style={{color: "white"}}>Cause:</strong> {alert.cause}
      </p> */}
      <button className="expand-btn"
        onClick={() => onExpand(alert)}
      >EXPAND</button>
    </div>
  );
};

export default AlertCard;
