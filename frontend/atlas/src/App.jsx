import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ProtectedRoute from './components/ProtectedRoute';

import Home from './pages/Home';
import Crew from './pages/Crew';
import Communication from './pages/Communication';
import Alerts from './pages/Alerts';
import Reports from './pages/Reports';
import Data from './pages/Data';

import Login from './components/Login';
import Signup from './components/Signup';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Rotas públicas */}
        <Route path='/' element={<Login />} />
        <Route path='/signup' element={<Signup />} />

        {/* Rotas protegidas */}
        <Route
          path='/home'
          element={
            <ProtectedRoute allowedRoles={['ASTRONAUT', 'FLIGHT_DIRECTOR']}>
              <Home />
            </ProtectedRoute>
          }
        />
        <Route
          path='/crew'
          element={
            <ProtectedRoute allowedRoles={['ASTRONAUT', 'FLIGHT_DIRECTOR']}>
              <Crew />
            </ProtectedRoute>
          }
        />
        <Route
          path='/alerts'
          element={
            <ProtectedRoute allowedRoles={['ASTRONAUT', 'FLIGHT_DIRECTOR']}>
              <Alerts />
            </ProtectedRoute>
          }
        />
        <Route
          path='/reports'
          element={
            <ProtectedRoute allowedRoles={['CEO', 'FLIGHT_DIRECTOR']}>
              <Reports />
            </ProtectedRoute>
          }
        />
        <Route
          path='/communication'
          element={
            <ProtectedRoute allowedRoles={['ASTRONAUT', 'FLIGHT_DIRECTOR']}>
              <Communication />
            </ProtectedRoute>
          }
        />
        <Route
          path='/data'
          element={
            <ProtectedRoute allowedRoles={['CEO', 'FLIGHT_DIRECTOR']}>
              <Data />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;