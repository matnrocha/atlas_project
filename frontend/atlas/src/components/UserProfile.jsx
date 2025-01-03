import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../atlas.css';


const UserProfile = () => {
  const [userDetails, setUserDetails] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserDetails = async () => {
      const storedData = localStorage.getItem('userDetails');
      if (storedData) {
        setUserDetails(JSON.parse(storedData));
      } else {
        try {
          const token = localStorage.getItem('token');
          const url = import.meta.env.VITE_API_URL;

          if (!token) {
            throw new Error('No token found');
          }

          const response = await axios.get(`${url}/api/v1/users/details`, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });

          localStorage.setItem('userDetails', JSON.stringify(response.data));
          setUserDetails(response.data);
        } catch (err) {
          console.error('Error fetching user details:', err);
          setError(err.message);
        }
      }
    };

    fetchUserDetails();
  }, []);

  if (error) {
    return <div className="user-profile-error">Error while loading user details.</div>;
  }

  if (!userDetails) {
    return <div>Loading...</div>;
  }

  const formattedRole = userDetails.role.replace(/_/g, ' ');

  return (
    <div className="user-profile-box">
      <div className="user-profile-content">
        <p>{userDetails.name} | {formattedRole}</p>
      </div>
    </div>
  );
};

export default UserProfile;
