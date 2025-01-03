import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../login.css';

import myLogo from '../assets/atlas_logo.png';

const Signup = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [preferredRole, setPreferredRole] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault();
    setError('');

    // Verifica se algum campo está vazio
    if (!email || !password || !fullName || !preferredRole) {
      setError('Please fill all the fields.');
      return;
    }


    try {
      const url = import.meta.env.VITE_API_URL;
      const response = await fetch(`${url}/api/v1/auth/signup`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email,
          password,
          fullName,
          preferredRole,
        }),
      });

      if (!response.ok) {
        throw new Error('We could not create your account. Please try again.');
      }

      
      navigate('/');
    } catch (err) {
      setError(err.message);
    }
  };

  const goToLogin = () => {
    navigate('/');
  };

  return (
    <div className="signup-container">
      <form onSubmit={handleSignup} className="signup-form">
        <img className="logo" src={myLogo} alt="Logo" />
        <h1 className="title">Signup</h1>
        {error && <p className="error">{error}</p>}
        <div className="input-group">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
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
          />
        </div>
        <div className="input-group">
          <label htmlFor="full_name">Full Name</label>
          <input
            type="text"
            id="full_name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
          />
        </div>
        <div className="role">
          <label htmlFor="preferred_role">Role</label>
          <select
            id="preferred_role"
            value={preferredRole}
            onChange={(e) => setPreferredRole(e.target.value)}
            required
          >
            <option value="">Select</option>
            <option value="CEO">CEO</option>
            <option value="FLIGHT_DIRECTOR">Flight Director</option>
          </select>
        </div>
        <button type="submit" className="signup-button">
          Signup
        </button>
        <div className="login-link">
            <button onClick={goToLogin} className="login-button">
            Login
            </button>
        </div>
      </form>
    </div>
  );
};

export default Signup;
