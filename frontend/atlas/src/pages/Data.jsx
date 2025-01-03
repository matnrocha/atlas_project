import React, { useState, useEffect } from "react";
import "../atlas.css";
import Navbar from "../components/Navbar";
import Status from "../components/Status";
import logo from "../assets/atlas_logo.png";
import { LineChart } from "@mui/x-charts/LineChart";
import { axisClasses } from "@mui/x-charts/ChartsAxis";
import UserProfile from "../components/UserProfile";
import Logout from "../components/Logout";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

function Data() {
    const maxPoints = 11;

    const sensorOptions = [
        { value: "cabinTemperature", label: "Cabin Temp (°C)" },
        { value: "cabinPressure", label: "Cabin Pressure (atm)" },
        { value: "co2Level", label: "CO2 Level (%)" },
        { value: "ppo2Level", label: "PPO2 Level (%)" },
        { value: "intTemperature", label: "Internal Temp (°C)" },
        { value: "extTemperature", label: "External Temp (°C)" },
        { value: "humidity", label: "Humidity (%)" },
        { value: "battery", label: "Battery (%)" },
        { value: "velocity", label: "Velocity (m/s)" },
        { value: "altitude", label: "Altitude (m)" },
        { value: "apogee", label: "Apogee (m)" },
        { value: "perigee", label: "Perigee (m)" },
        { value: "inclination", label: "Inclination (°)" },
        { value: "range", label: "Range (AU)" },
    ];

    const colorPalette = ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF", "#FF9F40"];

    const [graphData, setGraphData] = useState({
        timestamps: [],
        cabinTemperature: [],
        cabinPressure: [],
        co2Level: [],
        ppo2Level: [],
        intTemperature: [],
        extTemperature: [],
        humidity: [],
        battery: [],
        velocity: [],
        altitude: [],
        apogee: [],
        perigee: [],
        inclination: [],
        range: [],
    });

    const [selectedSeries, setSelectedSeries] = useState(() => {
        // Load initial state from localStorage or default to specific series
        const savedSeries = localStorage.getItem("selectedSeries");
        return savedSeries ? JSON.parse(savedSeries) : ["cabinTemperature", "co2Level", "humidity", "velocity"];
    });

    useEffect(() => {
        const url = import.meta.env.VITE_API_URL; // Replace with your WebSocket server URL
        const socket = new SockJS(`${url}/ws`);
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            console.log("Connected to WebSocket");
            stompClient.subscribe("/topic/sensor", (message) => {
                try {
                    const data = JSON.parse(message.body);
            
                    if (data) {
                        // Update graph data dynamically
                        setGraphData((prevData) => {
                            const updatedData = { ...prevData };
                            for (const key in data) {
                                if (key in prevData) {
                                    updatedData[key] = [
                                        ...prevData[key].slice(-maxPoints + 1),
                                        data[key],
                                    ];
                                }
                            }
                            updatedData.timestamps = [
                                ...prevData.timestamps.slice(-maxPoints + 1),
                                new Date(data.timestamp)
                            ];
                            localStorage.setItem("selectedSeries", JSON.stringify(selectedSeries));
                            return updatedData;
                        });
                    }
                } catch (error) {
                    console.error("Failed to process WebSocket message:", error);
                }
            });
    });
        

        return () => {
            if (stompClient) {
                stompClient.disconnect(() => {
                    console.log("WebSocket disconnected");
                });
            }
        };
    }, []);

    // useEffect(() => {
    //     // Save selectedSeries to localStorage whenever it changes
    //     localStorage.setItem("selectedSeries", JSON.stringify(selectedSeries));
    // }, [selectedSeries]);

    const commonAxisStyle = {
        [`.${axisClasses.root}`]: {
            [`.${axisClasses.tick}, .${axisClasses.line}`]: {
                stroke: "#ffffff",
                strokeWidth: 3,
            },
            [`.${axisClasses.tickLabel}`]: {
                fill: "#ffffff",
            },  
        },
    };

    const handleSelectionChange = (index, value) => {
        const updatedSelection = [...selectedSeries];
        updatedSelection[index] = value;
        setSelectedSeries(updatedSelection);
    };

    const availableOptions = (currentIndex) =>
        sensorOptions.filter(
            (option) =>
                !selectedSeries.includes(option.value) || selectedSeries[currentIndex] === option.value
        );

    const formattedTimestamps = graphData.timestamps.map((timestamp) => //is not working
        new Date(timestamp).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", second: "2-digit" })
    );

    return (
        <div className="app-container">
            <div className="crew-container">
                <div className="header">A.T.L.A.S. DATA</div>

                <Status />

                <div style={{ display: "flex", justifyContent: "space-between" }}>
                    {/* Graphs Container */}
                    <div className="data-container">
                        <div style={{ textAlign: "center" }}>
                            <h3>Sensor Data Evolution</h3>
                        </div>
                        <table>
                            <tbody>
                                <tr>
                                    <td>
                                        <LineChart
                                            sx={commonAxisStyle}
                                            xAxis={[{
                                                data: graphData.timestamps,
                                                valueFormatter: (value) => new Date(value).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })
                                            }]}
                                            series={[
                                                {
                                                    data: graphData[selectedSeries[0]],
                                                    label: sensorOptions.find(
                                                        (opt) => opt.value === selectedSeries[0]
                                                    ).label,
                                                    color: colorPalette[0],
                                                },
                                            ]}
                                            slotProps={{
                                                legend: {
                                                  labelStyle: {
                                                    fill: 'white',
                                                  },
                                                },
                                                }}
                                            width={400}
                                            height={250}
                                            grid={{ vertical: true, horizontal: true }}
                                        />
                                    </td>
                                    <td>
                                        <LineChart
                                            sx={commonAxisStyle}
                                            xAxis={[{
                                                data: graphData.timestamps,
                                                valueFormatter: (value) => new Date(value).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })
                                            }]}
                                            series={[
                                                {
                                                    data: graphData[selectedSeries[1]],
                                                    label: sensorOptions.find(
                                                        (opt) => opt.value === selectedSeries[1]
                                                    ).label,
                                                    color: colorPalette[1],
                                                },
                                            ]}
                                            slotProps={{
                                                legend: {
                                                  labelStyle: {
                                                    fill: 'white',
                                                  },
                                                },
                                                }}
                                            width={400}
                                            height={250}
                                            grid={{ vertical: true, horizontal: true }}
                                        />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <LineChart
                                            sx={commonAxisStyle}
                                            xAxis={[{
                                                data: graphData.timestamps,
                                                valueFormatter: (value) => new Date(value).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })
                                            }]}
                                            series={[
                                                {
                                                    data: graphData[selectedSeries[2]],
                                                    label: sensorOptions.find(
                                                        (opt) => opt.value === selectedSeries[2]
                                                    ).label,
                                                    color: colorPalette[2],
                                                },
                                            ]}
                                            slotProps={{
                                                legend: {
                                                  labelStyle: {
                                                    fill: 'white',
                                                  },
                                                },
                                                }}
                                            width={400}
                                            height={250}
                                            grid={{ vertical: true, horizontal: true }}
                                        />
                                    </td>
                                    <td>
                                        <LineChart
                                            sx={commonAxisStyle}
                                            xAxis={[{
                                                data: graphData.timestamps,
                                                valueFormatter: (value) => new Date(value).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })
                                            }]}                                         
                                            series={[
                                                {
                                                    data: graphData[selectedSeries[3]],
                                                    label: sensorOptions.find(
                                                        (opt) => opt.value === selectedSeries[3]
                                                    ).label,
                                                    color: colorPalette[3],
                                                },
                                            ]}
                                            slotProps={{
                                                legend: {
                                                  labelStyle: {
                                                    fill: 'white',
                                                  },
                                                },
                                                }}
                                            width={400}
                                            height={250}
                                            grid={{ vertical: true, horizontal: true }}
                                        />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>   
                </div>
                <div className="expanded-alert" style={{marginBottom: "40px"}}>
                        <h4>Configure Graphs</h4>
                        {selectedSeries.map((series, index) => (
                            <div key={index} style={{ marginBottom: "20px" }}>
                                <label>
                                    Graph {index + 1}:
                                    <select
                                        className="astronaut-dropdown"
                                        value={series}
                                        onChange={(e) =>
                                            handleSelectionChange(index, e.target.value)
                                        }
                                        style={{ padding: "5px", marginTop: "9px" }}
                                    >
                                        {availableOptions(index).map((option) => (
                                            <option key={option.value} value={option.value}>
                                                {option.label}
                                            </option>
                                        ))}
                                    </select>
                                </label>
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

export default Data;
