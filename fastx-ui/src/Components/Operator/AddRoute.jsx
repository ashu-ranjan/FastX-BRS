import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaMapMarkerAlt, FaRoad, FaLocationArrow, FaRoute } from 'react-icons/fa';
import '../../css/addRoute.css'; // Include the CSS below

function AddRoute() {
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [distance, setDistance] = useState('');
  const navigate = useNavigate();

  const processSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post(
        'http://localhost:8080/fastx/api/bus-route/add',
        { origin, destination, distance },
        { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }
      );
      alert('Route added successfully');
      navigate('/operator/routes');
    } catch (error) {
      console.error('Error adding route', error);
      alert('Failed to add route');
    }
  };

  return (
    <div className="add-route-container">
      <div className="form-card">
        <div className="form-title">
          <FaRoute className="me-2" />
          Add New Route
        </div>

        <form onSubmit={processSubmit}>
          <div className="form-group">
            <FaMapMarkerAlt className="form-icon" />
            <input
              type="text"
              value={origin}
              onChange={(e) => setOrigin(e.target.value)}
              placeholder="Origin"
              required
            />
          </div>
          <div className="form-group">
            <FaLocationArrow className="form-icon" />
            <input
              type="text"
              value={destination}
              onChange={(e) => setDestination(e.target.value)}
              placeholder="Destination"
              required
            />
          </div>
          <div className="form-group input-with-icon">
            <FaRoad className="form-icon" />
            <input
              type="number"
              value={distance}
              onChange={(e) => setDistance(e.target.value)}
              placeholder="Distance (km)"
              required
            />
          </div>

          <button type="submit" className="submit-btn">
            Add Route
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddRoute;
