import React, { useState, useEffect } from "react";
import { Navigate, useLocation } from "react-router-dom";
import apiClient from "../api/api";
import rolePermissions from "../api/rolePermissions";

const ProtectedRoute = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [loading, setLoading] = useState(true);
  const location = useLocation(); 

  useEffect(() => {
    const checkAuthentication = async () => {
      try {
        const response = await apiClient.get("/users/me"); 

        if (response.status === 200) {
          setIsAuthenticated(true);
          setUserRole(response.data.role.name); 
        } else {
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error("Error while verifying authentication:", error);
        setIsAuthenticated(false);
      } finally {
        setLoading(false);
      }
    };

    checkAuthentication();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />; 
  }

  const currentPath = location.pathname;
  const allowedRoles = rolePermissions[currentPath] || [];

  if (!allowedRoles.includes(userRole)) {
    return (
      <div style={{ textAlign: "center", marginTop: "20px" }}>
        <h2>Access Denied</h2>
        <p>You do not have permission to access this page.</p>
      </div>
    );
  }

  return children; 
};

export default ProtectedRoute;
