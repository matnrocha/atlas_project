import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faCheckCircle,
  faHeartbeat,
  faGlobe,
  faThermometerHalf,
  faBatteryHalf,
  faSatelliteDish,
  faAirFreshener,
  faFan
} from '@fortawesome/free-solid-svg-icons';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

function Status() {
const [alerts, setAlerts] = useState([]);
const [lastUpdated, setLastUpdated] = useState({}); // Track last update time for each system

useEffect(() => {
  const url = import.meta.env.VITE_API_URL; // Base API URL from environment variable
  const socket = new SockJS(`${url}/ws`);
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/alerts', (message) => {
      try {
        const newAlert = JSON.parse(message.body);
        console.log('New alert received:', newAlert);

        setAlerts((prevAlerts) => {
          const updatedAlerts = prevAlerts.filter(
            (alert) => alert.id !== newAlert.id || newAlert.priority !== 'FIXED'
          );

          // Update last updated time for the system
          setLastUpdated((prevTimes) => ({
            ...prevTimes,
            [newAlert.shipSystem]: Date.now(),
          }));

          return [newAlert, ...updatedAlerts];
        });
      } catch (error) {
        console.error('Failed to process alert message:', error);
      }
    });
  });

  return () => {
    stompClient.disconnect();
  };
}, []);

useEffect(() => {
  const interval = setInterval(() => {
    setLastUpdated((prevTimes) => {
      const now = Date.now();
      const updatedTimes = { ...prevTimes };

      // Reset systems that haven't been updated in 30 seconds
      Object.keys(updatedTimes).forEach((system) => {
        if (now - updatedTimes[system] > 10000) {
          delete updatedTimes[system]; // Remove from lastUpdated
          setAlerts((prevAlerts) =>
            prevAlerts.filter((alert) => alert.shipSystem !== system)
          );
        }
      });

      return updatedTimes;
    });
  }, 1000); // Check every second

  return () => clearInterval(interval);
}, []);

const defaultStatus = {
  "LIFE SUPPORT": "Normal",
  "SURROUNDINGS AND NAVIGATION": "No threats detected",
  "VENTILATION": "Clear",
  "THERMAL SYSTEM": "Normal",
  "COMMUNICATIONS": "Stable",
  "POWER SYSTEM": "Battery level: Normal",
};

const systemIcons = {
  "LIFE SUPPORT": faHeartbeat,
  "SURROUNDINGS AND NAVIGATION": faGlobe,
  "VENTILATION": faFan,
  "THERMAL SYSTEM": faThermometerHalf,
  "POWER SYSTEM": faBatteryHalf,
  "COMMUNICATIONS": faSatelliteDish,
};

const systemStatuses = { ...defaultStatus };
const systemColors = {};

alerts.forEach((alert) => {
  const system = alert.shipSystem.replace("_", " ").toUpperCase();
  if (alert.priority === "HIGH") {
    systemStatuses[system] = "Critical Issue";
    systemColors[system] = "rgb(217, 35, 35)";
  } else if (alert.priority === "MEDIUM") {
    systemStatuses[system] = "Warning";
    systemColors[system] = "#ffa500";
  } else if (alert.priority === "LOW") {
    systemStatuses[system] = "Needs attention";
    systemColors[system] = "#d8ba32";
  } else if (alert.priority === "FIXED") {
    systemStatuses[system] = "Resolved";
    systemColors[system] = "#40C110";
  }
});

// Generate status items dynamically
const statusItems = Object.keys(defaultStatus).map((system) => ({
  title: system,
  status: systemStatuses[system],
  icon: systemIcons[system],
  color: systemColors[system] || "#40C110",
}));


  return (
    <div className="status-section">
      {statusItems.map((item, index) => (
        <div
          key={index}
          className="status-item"
        >
          <span className="status-icon" style={{ color: '#9499C3' }}>
            <FontAwesomeIcon icon={item.icon} />
          </span>
          <strong style={{ color: item.color }}>{item.title}</strong><br />
          <span>{item.status}</span>
        </div>
      ))}
    </div>
  );
}

export default Status;
