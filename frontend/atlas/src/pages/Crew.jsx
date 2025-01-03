import React, { useState, useEffect } from "react";
import "../atlas.css";
import Navbar from "../components/Navbar";
import Status from "../components/Status";
import logo from "../assets/atlas_logo.png";
import body from "../assets/astronaut.png";
import Gauge from "../components/Gauge";
import { getAllAstronauts } from "../api/astronauts-consumer"; // Import the API method
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import UserProfile from "../components/UserProfile";
import Logout from "../components/Logout";

// Importing astronaut images
import saylor from "../assets/saylor.png";
import ana from "../assets/ana.png";
import juan from "../assets/juan.png";
import dua from "../assets/luadipa.png";
import joaocuco from "../assets/joaocuco.png";

function App() {
  const [astronauts, setAstronauts] = useState([]);
  const [selectedAstronaut, setSelectedAstronaut] = useState(astronauts[0]);
  const [vitals, setVitals] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Array of hardcoded astronaut images
  const astronautImages = [
    saylor,
    ana,
    juan,
    dua,
    joaocuco,
  ];

  // Fetch astronauts from the API
  useEffect(() => {
    const fetchAstronauts = async () => {
      try {
        const data = await getAllAstronauts(); // Fetch from API
        setAstronauts(data);
        if (data.length > 0) {
          setSelectedAstronaut(data[0]); // Set the first astronaut as selected
        }
      } catch (error) {
        console.error("Error fetching astronauts:", error);
      }
    };

    fetchAstronauts();
  }, []);

  useEffect(() => {
    if (!selectedAstronaut) return;

    const url = import.meta.env.VITE_API_URL;
    const socket = new SockJS(`${url}/ws`);
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      stompClient.subscribe("/topic/vitals", (message) => {
        try {
          const vitals = JSON.parse(message.body);

          if (vitals) {
            if (vitals.astronaut_id === selectedAstronaut.id)
              localStorage.setItem(`vitals_${selectedAstronaut.id}`, JSON.stringify(vitals));
            setVitals(vitals);
          }
        } catch (error) {
          console.error("Failed to process sensor data:", error);
        }
      });
    });
  }, [selectedAstronaut]);

  return (
    <div className="app-container">
      <div className="crew-container">
        <div className="header">A.T.L.A.S. CREW</div>

        <Status />

        <div className="metrics-container">
          {vitals ? (
            <>
              <div className="gauge-row">
                <Gauge label="Body Temp" minValue={35} maxValue={39} value={vitals.bodyTemperature} unit="°C" />
                <p>{selectedAstronaut?.name.toUpperCase()}'S VITALS</p>
                <Gauge label="Heart Rate" minValue={50} maxValue={120} value={vitals.heartRate} unit="BPM" />
              </div>

              <div className="gauge-row-with-rocket">
                <div className="gauge-column">
                  <Gauge label="Oxygen Level" minValue={90} maxValue={100} value={vitals.oxygenLevel} unit="%" />
                </div>
                <div className="rocket-container">
                  <img className="body-image" src={body} alt="Body" />
                </div>
                <div className="gauge-column">
                  <Gauge label="Blood Pressure" minValue={80} maxValue={120} value={vitals.bloodPressure} unit="mmHg" />
                </div>
              </div>
            </>
          ) : (
            <p>Loading Astronaut Vitals...</p>
          )}
        </div>

        <div className="astronaut-list">
          {astronauts.map((astronaut, index) => (
            <div
              key={astronaut.id}
              className={`astronaut ${selectedAstronaut?.id === astronaut.id ? "selected" : ""}`}
              onClick={() => {
                const savedVitals = localStorage.getItem(`vitals_${astronaut.id}`);
                if (savedVitals) {
                  setVitals(JSON.parse(savedVitals));
                } else {
                  setVitals(null); // Reset if no saved vitals
                }
                setSelectedAstronaut(astronaut);
              }}
            >
              {/* Assign the image from the hardcoded list */}
              <img
                src={astronautImages[index]} // Use index to pick the right image
                alt={astronaut.name}
                className="astronaut-image"
              />
              <div className="astronaut-info">
                <h4>{astronaut.name}</h4>
                <p>Role: <span>{astronaut.role}</span></p>
              </div>
            </div>
          ))}
        </div>

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
