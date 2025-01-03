import React, { useState } from 'react';
import { getLatestData } from '../api/latest-data-consumer';

const LatestDataComponent = () => {
    const [spaceshipId, setSpaceshipId] = useState('');
    const [latestData, setLatestData] = useState(null);
    const [error, setError] = useState(null);

    const handleFetchData = async () => {
        setError(null); // Reset error state before each fetch
        try {
            const data = await getLatestData(spaceshipId);
            setLatestData(data);
        } catch (err) {
            setError("Failed to fetch data");
            console.error(err);
        }
    };

    return (
        <div>
            <h1>Get Latest Spaceship Data</h1>
            <input
                type="text"
                placeholder="Enter spaceship ID"
                value={spaceshipId}
                onChange={(e) => setSpaceshipId(e.target.value)}
            />
            <button onClick={handleFetchData}>Fetch Data</button>

            {error && <p style={{ color: 'red' }}>{error}</p>}
            {latestData && (
                <div>
                    <h3>Latest Sensor Data:</h3>
                    <pre>{JSON.stringify(latestData, null, 2)}</pre>
                </div>
            )}
        </div>
    );
};

export default LatestDataComponent;
