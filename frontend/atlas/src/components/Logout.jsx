import { useNavigate } from 'react-router-dom';
import '../atlas.css';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { faSignOut } from '@fortawesome/free-solid-svg-icons';


const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    // localStorage.clear();

    navigate('/');
  };

  return (
    <button className="logout-button" onClick={handleLogout}>
        <FontAwesomeIcon icon={faSignOut} />
    </button>
  );
};

export default Logout;
