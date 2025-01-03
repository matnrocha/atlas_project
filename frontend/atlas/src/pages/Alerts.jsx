import { AnimatePresence } from "framer-motion";
import Navbar from "../components/Navbar";
import Status from "../components/Status";
import logo from "../assets/atlas_logo.png";
import AlertCard from "../components/AlertCard";
import AlertExpanded from "../components/AlertExpanded";
import { useState, useEffect } from "react";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import UserProfile from "../components/UserProfile";
import Logout from "../components/Logout";

function App() {

  let last = {
      "spaceship_id": 1,
      "priority": "MEDIUM",
      "status": "NOT FIXED",
      "shipSystem": "LIFE_SUPPORT",
      "cause": "Test alert",
      "timestamp": "2024-12-06T12:30:00",
      "resolved": false
    }

  const [expandedAlert, setExpandedAlert] = useState(null);
  const [alerts, setAlerts] = useState([]);
  const [alert, setAlert] = useState(() => {
    const savedData = localStorage.getItem("alert");
    return savedData ? JSON.parse(savedData) : last;
  });
  

  useEffect(() => {
    const url = import.meta.env.VITE_API_URL; // Base API URL from environment variable
    const socket = new SockJS(`${url}/ws`);
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      stompClient.subscribe("/topic/alerts", (message) => {
        try {
          const newAlert = JSON.parse(message.body); // Parse the incoming alert message
          if (newAlert) {
            localStorage.setItem('alert', JSON.stringify(newAlert));
            setAlert(newAlert);
            setAlerts((prevAlerts) => {
             const alerts = [newAlert, ...prevAlerts];
             console.log("Updated alerts:", alerts);
             return alerts.slice(0,10);
            });
          }
        } catch (error) {
          console.error("Failed to process alerts:", error);
        }
      });
    }
    );
    // Cleanup function to disconnect WebSocket on component unmount
    return () => {
      stompClient.disconnect();
      console.log("WebSocket disconnected");
    };
  }, []); 
  
  const handleExpandAlert = (alert) => {
    setExpandedAlert(alert);
  };

  const handleCloseAlert = () => {
    setExpandedAlert(null); // Close the expanded alert
  };

  if(!alert) {
    return <div>No alerts to display.</div>;
  }

  return (
    <div className="app-container">
      <div className="alert-container">
        <div className="header">A.T.L.A.S. ALERTS</div>
        <Status/>

        {alerts.length > 0 && (
          <div className="alerts-container">
            {alerts.map((alert, index) => (
              <AlertCard key={index} alert={alert} onExpand={handleExpandAlert} />
            ))}
          </div>
        )}


        <AnimatePresence>
          {expandedAlert && (
            <AlertExpanded alert={expandedAlert} onClose={handleCloseAlert} />
          )}
        </AnimatePresence>

        <Navbar />
        <div className="logo-section">
          <img className="logo-image" src={logo} alt="ATLAS Logo" />
        </div>
        <div className="user-section">
            <UserProfile />
            <Logout />
          </div>
      </div>
    </div>
  );
}

export default App;
