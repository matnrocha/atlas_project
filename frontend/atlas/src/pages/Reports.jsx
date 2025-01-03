import Navbar from "../components/Navbar";
import Status from "../components/Status";
import logo from "../assets/atlas_logo.png";
import { useState, useEffect } from "react";
import { getReport } from "../api/report-consumer";
import UserProfile from "../components/UserProfile";
import Logout from "../components/Logout";
import generatePDF, { Options } from "react-to-pdf";
import { AnimatePresence, motion } from "framer-motion";

const options = {
  filename: 'ATLAS_Report.pdf',
  page: {
    margin: 5,
  },
  x: 0,
  y: 0,
  width: "auto",
  height: "auto",
};

const getTargetElement = () => document.getElementById("report-template");

const downloadPdf = () => generatePDF(getTargetElement, options);

const NORMAL_RANGES = {
  cabinTemperature: { min: 17.0, max: 28.0, unit: "°C" },
  cabinPressure: { min: 0.95, max: 1.15, unit: "Pa" },
  co2Level: { min: 320.0, max: 480.0, unit: "ppm" },
  extTemperature: { min: -90.0, max: 40.0, unit: "°C" },
  humidity: { min: 25.0, max: 75.0, unit: "%" },
  battery: { min: 30.0, max: 95.0, unit: "%" },
  velocity: { min: 2000.0, max: 7000.0, unit: "m/s" },
  altitude: { min: 500.0, max: 300000.0, unit: "meters" }
};

function App() {
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [averages, setAverages] = useState(null);
  const [errorMsg, setErrorMsg] = useState("Please select a timeframe to generate a report.");

  // Load last report from local storage on page load
  useEffect(() => {
    const savedReport = localStorage.getItem("lastReport");
    if (savedReport) {
      const { averages, startTime, endTime } = JSON.parse(savedReport);
      setAverages(averages);
      setStartTime(startTime);
      setEndTime(endTime);
    }
  }, []);

  const handleApiCall = async () => {
    const startTimeFormatted = new Date(startTime).toISOString().slice(0, 19);
    const endTimeFormatted = new Date(endTime).toISOString().slice(0, 19);

    if (!startTime || !endTime) {
      setErrorMsg("Please select a timeframe to generate a report.");
      return;
    }

    if (startTime >= endTime) {
      setErrorMsg("End time must be after start time.");
      return;
    }

    const data = await getReport(1, startTimeFormatted, endTimeFormatted);

    if (data && data.length > 0) {
      generateReport(data);
    } else {
      setErrorMsg("No data available for the selected timeframe.");
      setAverages(null);
    }
  };

  const checkRange = (value, min, max) => value >= min && value <= max;


  const generateReport = (reports) => {
    const totals = reports.reduce((acc, report) => {
      acc.cabinTemperature += report.cabinTemperature || 0;
      acc.cabinPressure += report.cabinPressure || 0;
      acc.co2Level += report.co2Level || 0;
      acc.extTemperature += report.extTemperature || 0;
      acc.humidity += report.humidity || 0;
      acc.battery += report.battery || 0;
      acc.velocity += report.velocity || 0;
      acc.altitude += report.altitude || 0;
      return acc;
    }, {
      cabinTemperature: 0,
      cabinPressure: 0,
      co2Level: 0,
      extTemperature: 0,
      humidity: 0,
      battery: 0,
      velocity: 0,
      altitude: 0
    });

    const count = reports.length;

    const calculatedAverages = {
      cabinTemperature: (totals.cabinTemperature / count).toFixed(2),
      cabinPressure: (totals.cabinPressure / count).toFixed(2),
      co2Level: (totals.co2Level / count).toFixed(2),
      extTemperature: (totals.extTemperature / count).toFixed(2),
      humidity: (totals.humidity / count).toFixed(2),
      battery: (totals.battery / count).toFixed(2),
      velocity: (totals.velocity / count).toFixed(2),
      altitude: (totals.altitude / count).toFixed(2),
      count
    };

    const messages = Object.keys(NORMAL_RANGES).map((metric) => {
      const average = parseFloat(calculatedAverages[metric]);
      const { min, max, unit } = NORMAL_RANGES[metric];
      if (checkRange(average, min, max)) {
        return `<strong>${metric}</strong> was <strong style={{ color: "#00bfff" }}>${average} ${unit} </strong> - Within the normal range (${min} - ${max}).`;
      } else {
        return `<strong>${metric}</strong> was <strong style={{ color: "#00bfff" }}>${average} ${unit} </strong> - Outside the normal range (${min} - ${max}).`;
      }
    });

    const reportData = { averages: { ...calculatedAverages, messages }, startTime, endTime };

    // Save to local storage
    localStorage.setItem("lastReport", JSON.stringify(reportData));

    setAverages({ ...calculatedAverages, messages });
  };

  return (
    <div className="app-container">
      <div className="alert-container">
        <div className="header">A.T.L.A.S. REPORTS</div>

        <Status />

        <div className="reports-container">
          <div className="input-row">
            <div className="input-group">
              <label htmlFor="start-time">Start Time</label>
              <input
                type="datetime-local"
                id="start-time"
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)}
                className="input-field"
              />
            </div>
            <div className="input-group">
              <label htmlFor="end-time">End Time</label>
              <input
                type="datetime-local"
                id="end-time"
                value={endTime}
                onChange={(e) => setEndTime(e.target.value)}
                className="input-field"
              />
            </div>
            <div className="input-group">
              <label htmlFor="b">&nbsp;</label>
              <button id="b" onClick={handleApiCall} className="button-primary">
                Create Reports
              </button>
            </div>
          </div>

          {averages && (
            <div className="report-template" id="report-template">
              <h3>A.T.L.A.S. SensorData Report</h3>
              <p><strong style={{ color: "#00bfff" }}>Timeframe:</strong> {new Date(startTime).toLocaleString()} to {new Date(endTime).toLocaleString()}</p>

              <p>During the specified timeframe, the onboard sensors recorded the following averages and observations:</p>

              <ul>
                {averages.messages.map((msg, index) => (
                  <li key={index} dangerouslySetInnerHTML={{ __html: msg }}></li>
                ))}
              </ul>
              <p><strong style={{ color: "#00bfff" }}>Summary:&nbsp;</strong>
                Overall, the spacecraft systems operated {averages.messages.some((msg) => msg.includes("outside")) ? "with anomalies" : "nominally"} during the observed timeframe.
                The recorded metrics confirm that {averages.messages.some((msg) => msg.includes("outside")) ? "some systems require attention" : "all critical systems are functioning as expected"}.
                A total of <strong>{averages.count}</strong> readings were analyzed.
              </p>
              <p><strong style={{ color: "#00bfff" }}>Date:</strong> {new Date().toLocaleDateString()}</p>
              <p>
                <strong style={{ color: "#00bfff" }}>Reported by:</strong> A.T.L.A.S. Automatic Report System
            </p>            
            </div>
          )}

          {!averages && (
            <div className="metrics-container">
              <p>{errorMsg}</p>
            </div>
          )}
        </div>

        <AnimatePresence>
          {averages && (
            <motion.div
              className="pdf-area"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: 20 }}
              style={{ marginBottom: "40px" }}
            >
              <h4 className="alert-title"><strong>Export PDF</strong></h4>
                <p>Click the button below to download the report as a PDF.</p>
              <button className="pdf-button" onClick={downloadPdf}>Download</button>
            </motion.div>
          )}
        </AnimatePresence>

        <Navbar />

        <div className="logo-section">
          <img src={logo} alt="ATLAS Logo" className="logo-image" />
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