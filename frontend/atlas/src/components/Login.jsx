import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../login.css';

import myLogo from '../assets/atlas_logo.png';
import rolePermissions from '../api/rolePermissions';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('userDetails');
    localStorage.clear();
  }, []);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
  
    try {
      const url = import.meta.env.VITE_API_URL;
      const response = await fetch(`${url}/api/v1/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });
  
      const data = await response.json();
  
      if (!response.ok) {
        const errorMessage = data.message || 'Authentication failed.';
        throw new Error(errorMessage);
      }
  
      if (!data.token) {
        throw new Error('Authentication token not received');
      }
  
      localStorage.setItem('token', data.token);
  
      const userDetailsResponse = await fetch(`${url}/api/v1/users/details`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${data.token}`,
        },
      });
  
      const userDetails = await userDetailsResponse.json();
  
      if (!userDetailsResponse.ok) {
        throw new Error('Error fetching user details');
      }
  
      const userRole = userDetails.role;
  
      console.log('User details:', userDetails);
  
      const allowedRoutes = Object.keys(rolePermissions).filter(route =>
        rolePermissions[route].includes(userRole)
      );
  
      console.log("Allowed routes for this role: ", allowedRoutes);
  
      if (allowedRoutes.length > 0) {
        navigate(allowedRoutes[0]);
      } else {
        navigate('/home');
      }
  
    } catch (err) {
      const errorMap = {
        'Invalid Credentials': 'Incorrect email or password',
        'User not found': 'User not registered',
        'Account locked': 'Blocked account',
        'default': 'Error logging in. Please try again.'
      };
  
      const errorMessage = errorMap[err.message] || errorMap['default'];
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  const goToSignup = () => {
    navigate('/signup');
  };

  return (
    <div className="login-container">
      <form onSubmit={handleLogin} className="login-form">
        <img className="logo" src={myLogo} alt="Logo" />
        <h1 className="title">Login</h1>
        {error && <p className="error">{error}</p>}
        <div className="input-group">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            disabled={isLoading}
          />
        </div>
        <div className="input-group">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            disabled={isLoading}
          />
        </div>
        <button 
          type="submit" 
          className="signup-button"
          disabled={isLoading}
        >
          {isLoading ? 'Loading...' : 'Login'}
        </button>
        <div className="login-link">
          <button 
            onClick={goToSignup} 
            className="login-button"
            disabled={isLoading}
          >
            Signup
          </button>
        </div>
      </form>
    </div>
  );
};

export default Login;
