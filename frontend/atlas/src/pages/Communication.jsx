import React, { useEffect, useState } from "react";
import "../atlas.css";
import Navbar from "../components/Navbar";
import Status from "../components/Status";
import logo from "../assets/atlas_logo.png";
import satellite from "../assets/satellite.png";
import { LineChart } from "@mui/x-charts/LineChart";
import { axisClasses } from "@mui/x-charts/ChartsAxis";
import UserProfile from "../components/UserProfile";
import Logout from "../components/Logout";
import {
  // getMessages,
  // getMessagesByMatchingSubstring,
  // getMessagesBySenderName,
  // getMessagesByMatchingSubstringAndSender,
  getNLatestMessages,
  getNLatestMessagesByMatchingSubstring,
  getNLatestMessagesBySenderName,
  getNLatestMessagesByMatchingSubstringAndSender,
  getNLatestSystemMessages
} from "../api/communication-consumer";
import { getAllAstronauts } from "../api/astronauts-consumer";
// Websocket imports
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

function App() {
  // const [userMessages, setUserMessages] = useState(() => {
  //   const localStorageMessages = localStorage.getItem("UserMessages");
  //   console.log("local storage user messages:", JSON.parse(localStorageMessages));
  //   return localStorageMessages ? JSON.parse(localStorageMessages) : [];
  // });
  const [userMessages, setUserMessages] = useState([]);
  const [systemMessages, setSystemMessages] = useState(() => {
    const localStorageMessages = localStorage.getItem("SystemMessages");
    return localStorageMessages ? JSON.parse(localStorageMessages) : [];
  });
  const [searchTerm, setSearchTerm] = useState("");
  const [astronauts, setAstronauts] = useState([]);
  const [selectedAstronaut, setSelectedAstronaut] = useState("All Astronauts");
  const interval = 3000;
  const numSystemMessages = 8;   
  const numUserMessages = 30;  
  // Fetch all astronauts once
  const fetchAstronauts = async () => {
    try {
      const data = await getAllAstronauts();
      setAstronauts(data);
    } catch (error) {
      console.error("Error fetching astronauts:", error);
    }
  };

  // const saveUserMessageToLocalStorage = (message) => {
  //   const localUserMessages = JSON.parse(localStorage.getItem("UserMessages")) || [];
  //   localUserMessages.push(message);
  
  //   // Limit stored messages to a fixed number for performance reasons, e.g., 100 messages.
  //   if (localUserMessages.length > 100) {
  //     localUserMessages.shift();
  //   }
  
  //   localStorage.setItem("UserMessages", JSON.stringify(localUserMessages));
  // };

  const fetchFilteredMessages = async (astronautName, term) => {
    try {
      let userMsgs;
      let systemMsgs;

      if (astronautName === "All Astronauts" && !term) {
        userMsgs = await getNLatestMessages(1, numUserMessages);
        userMsgs = userMsgs.filter((message) => message.sender !== null);
      } else if (astronautName === "All Astronauts") {
        userMsgs = await getNLatestMessagesByMatchingSubstring(1, numUserMessages, term);
      } else if (!term) {
        userMsgs = await getNLatestMessagesBySenderName(1, numUserMessages, astronautName);
      } else {
        userMsgs = await getNLatestMessagesByMatchingSubstringAndSender(1, numUserMessages, astronautName, term);
      }

      systemMsgs = await getNLatestSystemMessages(1, numSystemMessages);

      setUserMessages(userMsgs);
      setSystemMessages(systemMsgs);

    } catch (error) {
      console.error("Error fetching filtered messages:", error);
    }
  };
  


  // Handle changes in search term and apply appropriate filters
  const handleSearchChange = async (e) => {
    const substring = e.target.value;
    setSearchTerm(substring);
    await fetchFilteredMessages(selectedAstronaut, substring);
  };


  // Handle changes in selected astronaut and apply appropriate filters
  const handleAstronautChange = async (e) => {
    const selected = e.target.value;
    setSelectedAstronaut(selected);
    await fetchFilteredMessages(selected);
  };
  

  // Set up intervals for fetching data
  useEffect(() => {
    fetchAstronauts();
  
    const url = import.meta.env.VITE_API_URL;
    const socket = new SockJS(`${url}/ws`);
    const stompClient = Stomp.over(socket);
  
    stompClient.connect({}, () => {
      stompClient.subscribe("/topic/messages", (message) => {
        try {
          const parsedMessage = JSON.parse(message.body);
  
          if (parsedMessage) {
            if (parsedMessage.sender_id !== 0) {
              // saveUserMessageToLocalStorage(parsedMessage);
              // setUserMessages((prevMessages) => [...prevMessages, parsedMessage]);
              // // Optionally, refetch filtered messages if necessary
              // fetchFilteredMessages(selectedAstronaut, searchTerm);
              localStorage.setItem(`message_${parsedMessage.id}`, JSON.stringify(parsedMessage));
              // handleIncomingMessage(parsedMessage);
              fetchFilteredMessages(selectedAstronaut, searchTerm);
            } else {
              const localSystemMessages = JSON.parse(localStorage.getItem("SystemMessages")) || [];
              localSystemMessages.push(parsedMessage);
              if (localSystemMessages.length > numSystemMessages) {
                const localSystemMessages = localSystemMessages.shift();
              }
              localStorage.setItem("SystemMessages", JSON.stringify(localSystemMessages));
            }
          }
        } catch (error) {
          console.error("Failed to process sensor data:", error);
        }
      });
    });
  
    return () => {
      stompClient.disconnect();
    };
  }, [searchTerm, selectedAstronaut]); // React will re-subscribe when these change

  return (
    <div className="app-container">
      <div className="crew-container">
        <div className="header">A.T.L.A.S. COMMUNICATION</div>

        <Status />

        <div className="comms-container">
          <div className="graph-row">
            <div className="response-time-graph">
              <LineChart
                sx={{
                  [`.${axisClasses.root}`]: {
                    [`.${axisClasses.tick}, .${axisClasses.line}`]: { stroke: "#ffffff", strokeWidth: 3 },
                    [`.${axisClasses.tickLabel}`]: { fill: "#ffffff" },
                  },
                }}
                xAxis={[
                  {
                    scaleType: "band", // Ensures discrete time values are shown properly
                    data: systemMessages.map((message) =>
                      new Date(message.timestamp).toLocaleTimeString([], {
                        hour: "2-digit",
                        minute: "2-digit",
                        second: "2-digit",
                      })
                    ),
                  },
                ]}
                series={[
                  {
                    data: systemMessages.map((message) => parseFloat(message.message)), // Response time values
                    color: "#2196f3", // Optional: Sets line color to blue
                    label: "Response Time (ms)", // Tooltip label
                    showMark: true, // Ensures data points are visible as dots
                  },
                ]}
                yAxis={[{
                  colorMap: {
                    type: 'piecewise',
                    thresholds: [1000, 1200, 1500],
                    colors: ['blue', 'yellow', 'orange', 'red'],
                  }
                }]}
                slotProps={{
                  legend: {
                    labelStyle: {
                      fill: 'white',
                    },
                  },
                  }}
                width={800}
                height={300}
                grid={{ vertical: true, horizontal: true }}
                tooltip={{ // Customizes the tooltip content
                  trigger: "item",
                  formatter: (params) => {
                    return `
                      <div>
                        <b>${params.seriesLabel}</b>: ${params.value} ms
                      </div>
                    `;
                  },
                }}
              />
            </div>
          </div>

          <div className="gauge-row-with-rocket">
            <div className="gauge-column"></div>
            <div className="rocket-container">
              <img className="satellite-image" src={satellite} alt="Satellite" />
            </div>
            <div className="gauge-column"></div>
          </div>
        </div>

        <div className="intercom-log-section">
          <div className="intercom-log-header">
            <input
              type="text"
              className="intercom-search"
              placeholder="Search for messages"
              value={searchTerm}
              onChange={handleSearchChange}
            />
            <select
              className="astronaut-dropdown"
              value={selectedAstronaut}
              onChange={handleAstronautChange}
            >
              <option value="All Astronauts">All Astronauts</option>
              {astronauts.map((astronaut) => (
                <option key={astronaut.id} value={astronaut.name}>
                  {astronaut.name}
                </option>
              ))}
            </select>
          </div>
          <div className="intercom-log">
            {userMessages
              .reverse() // Reverse the order of messages
              .map((message, index) => (
                <div key={index} className="intercom-log-item">
                  <p>
                    <span className="log-time">
                      [{new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })}]
                    </span>
                    <span className="log-name">{message?.sender?.name || "Unknown Sender"}:</span>
                    <span className="log-message">“{message.message}”</span>
                  </p>
                </div>
              ))}
          </div>  
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
