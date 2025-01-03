import { useState, useEffect } from "react";
import Gauge from "../components/Gauge";
import ProgressBar from "../components/ProgressBar";
import "../atlas.css";
import Navbar from "../components/Navbar";
import Status from "../components/Status";
import spaceship from "../assets/astrogato.png";
import logo from "../assets/atlas_logo.png";
import { getLatestData } from "../api/latest-data-consumer";
import UserProfile from "../components/UserProfile";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import Logout from "../components/Logout";


function App() {

let last= {
    "spaceship_id": 1,
    "cabinTemperature": 0,
    "cabinPressure": 0,
    "co2Level": 0,
    "ppo2Level": 0,
    "intTemperature": 0,
    "extTemperature": 0,
    "humidity": 0,
    "battery": 0,
    "velocity": 0,
    "altitude": 0,
    "apogee": 0,
    "perigee": 0,
    "inclination": 0,
    "range": 0,
    "timestamp": "2024-12-01T22:37:33",
}

const [sensorData, setSensorData] = useState(() => {
  const savedData = localStorage.getItem('sensorData');
  return savedData ? JSON.parse(savedData) : last;
});

// const [sensorData, setSensorData] = useState(null);

  // Fetch sensor data on component mount and periodically
  // useEffect(() => {
  //   const fetchData = async () => {
  //     const data = await getLatestData(1); // Replace with dynamic spaceship ID if needed
  //     if (data) {
  //       setSensorData(data);
  //        console.log(data);           //TIRAR ISSO DEPOIS
  //     }
  //   };

  //   fetchData();
  //   const intervalId = setInterval(fetchData, 2000); // Update every 2 seconds

  //   return () => clearInterval(intervalId); // Cleanup interval on unmount
  // }, []);

useEffect(() => {
  const url = import.meta.env.VITE_API_URL;
  const socket = new SockJS(`${url}/ws?`);
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe("/topic/sensor", (message) => {
      try {
        const sensor = JSON.parse(message.body);

        if (sensor) {
          localStorage.setItem('sensorData', JSON.stringify(sensor));
          setSensorData(sensor);
        }
        } catch (error) {
          console.error("Failed to process sensor data:", error);
        }
      });
    });
  });


  if (!sensorData) {
    return <div>No sensor data to display.</div>;
  }

  return (
    <div className="app-container">
      <div className="dashboard-container">
          <div className="header">A.T.L.A.S. OVERVIEW</div>
        
        <Status/>

        <div className="metrics-container">
          <div className="gauge-row">
            {/* Dynamic values from API */}
            <Gauge label="Cabin Temp" minValue={15} maxValue={30} value={sensorData.cabinTemperature} unit="°C" />
            <Gauge label="Cabin Pressure" minValue={0} maxValue={2.0} value={sensorData.cabinPressure} unit="psia" />
            <Gauge label="CO2" minValue={300} maxValue={500.0} value={sensorData.co2Level} unit="mmHg" />
            <Gauge label="PPO2" minValue={18} maxValue={25} value={sensorData.ppo2Level} unit="psia" />
          </div>

          <div className="gauge-row-with-rocket">
            <div className="gauge-column">
              <Gauge label="Int. Temp" minValue={10} maxValue={35} value={sensorData.intTemperature} unit="°C" />
            </div>
            <div className="gauge-column">
              <Gauge label="Ext. Temp" minValue={-100} maxValue={0} value={sensorData.extTemperature} unit="°C" />
            </div>
            <div className="rocket-container">
              <img className="rocket-placeholder" src={spaceship} alt="Rocket" />
            </div>
            <div className="gauge-column">
              <Gauge label="Humidity" maxValue={100} value={sensorData.humidity} unit="%" />
            </div>
            <div className="gauge-column">
              <Gauge label="Battery" value={sensorData.battery} unit="%" />
            </div>
          </div>
        </div>

        <div className="progress-section">
          {/* Dynamic progress bars with new max values */}
          <ProgressBar label="Inertial Velocity" value={sensorData.velocity} max={7200} unit="km/s" />
          <ProgressBar label="Altitude" value={sensorData.altitude} max={32000} unit="km" />
          <ProgressBar label="Apogee" value={sensorData.apogee} max={450000} unit="km" />
          <ProgressBar label="Perigee" value={sensorData.perigee} max={300000} unit="km" />
          <ProgressBar label="Inclination" value={sensorData.inclination} max={180} unit="°" />
          <ProgressBar label="Range to ISS" value={sensorData.range} max={2500} unit="km" />
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