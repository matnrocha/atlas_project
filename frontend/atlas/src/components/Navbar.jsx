import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTachometerAlt, faUserAstronaut, faSatellite, faBell, faFileAlt, faDatabase } from '@fortawesome/free-solid-svg-icons';
import '../atlas.css';
import apiClient from "../api/api"; // Ajuste se necessário
import rolePermissions from '../api/rolePermissions';

function Navbar() {
  const location = useLocation(); // Get current path
  const [role, setRole] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserRole = async () => {
      try {
        console.log("Fazendo requisição para /users/me...");
        const response = await apiClient.get("/users/me");
        console.log("Resposta da API:", response);
        
        if (response.status === 200) {
          console.log("Role do usuário:", response.data.role.name);
          setRole(response.data.role.name || null);
        }
      } catch (error) {
        console.error("Erro ao verificar role do usuário:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchUserRole();
  }, []);

  const checkRoleAccess = (page) => {
    const allowedRoles = rolePermissions[page] || [];
    return allowedRoles.includes(role); 
  };

  if (loading) return <div>Loading...</div>;

  return (
    <nav className="navbar">
      <ul className="navbar-links">
        <li className={location.pathname === "/home" ? "active" : ""}>
          <Link to="/home" className={checkRoleAccess("/home") ? "" : "disabled"}>
            <FontAwesomeIcon icon={faTachometerAlt} className="nav-icon" /><br/> <strong> OVERVIEW </strong>
          </Link>
        </li>
        <li className={location.pathname === "/crew" ? "active" : ""}>
          <Link to="/crew" className={checkRoleAccess("/crew") ? "" : "disabled"}>
            <FontAwesomeIcon icon={faUserAstronaut} className="nav-icon" /><br/> <strong> CREW </strong>
          </Link>
        </li>
        <li className={location.pathname === "/communication" ? "active" : ""}>
          <Link to="/communication" className={checkRoleAccess("/communication") ? "" : "disabled"}>
            <FontAwesomeIcon icon={faSatellite} className="nav-icon" /><br/> <strong> COMMS </strong>
          </Link>
        </li>
        <li className={location.pathname === "/alerts" ? "active" : ""}>
          <Link to="/alerts" className={checkRoleAccess("/alerts") ? "" : "disabled"}>
            <FontAwesomeIcon icon={faBell} className="nav-icon" /><br/> <strong> ALERTS </strong>
          </Link>
        </li>
        <li className={location.pathname === "/reports" ? "active" : ""}>
          <Link to="/reports" className={checkRoleAccess("/reports") ? "" : "disabled"}>
            <FontAwesomeIcon icon={faFileAlt} className="nav-icon" /><br/> <strong>REPORTS </strong>
          </Link>
        </li>
        <li className={location.pathname === "/data" ? "active" : ""}>
          <Link to="/data" className={checkRoleAccess("/data") ? "" : "disabled"}>
            <FontAwesomeIcon icon={faDatabase} className="nav-icon" /><br/> <strong> DATA </strong>
          </Link>
        </li>
      </ul>
    </nav>
  );
}

export default Navbar;
