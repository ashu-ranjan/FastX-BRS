import axios from 'axios';
import { useEffect, useState } from 'react';
import { FaBusAlt, FaChartBar, FaClipboardList, FaFacebook, FaInstagram, FaLinkedin, FaTwitter } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import '../../css/Dashboard.css';

function Dashboard() {
  const [buses, setBuses] = useState([]);
  const name = localStorage.getItem("name") || "Operator";

  useEffect(() => {
    const fetchBuses = async () => {
      try {
        const response = await axios("http://localhost:8080/fastx/api/bus/get-buses", {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
        });
        setBuses(response.data);
      } catch (error) {
        console.error("Error fetching buses:", error);
      }
    };
    fetchBuses();
  }, []);

  return (
    <div className="dashboard-container">
      {/* 1. Header Section */}
      <div className="welcome-section">
        <div className="welcome-content">
          <h1 className="heading-title">
            <span className="heading-icon-wrapper">
              <FaBusAlt className="heading-icon" />
            </span>
            Welcome, <span className="highlight-name">{name}</span>
          </h1>
          <p className="welcome-subtitle">Manage your fleet and monitor bookings seamlessly.</p>
        </div>
      </div>

      {/* 2. Buses List */}
      <div className="dashboard-section">
        <h2 className="section-title">Your Fleet</h2>
        <div className="buses-grid">
          {buses.length > 0 ? buses.map((bus) => (
            <div className="bus-card" key={bus.id}>
              <div className="bus-card-content">
                <div className="bus-icon-wrapper">
                  <FaBusAlt className="bus-icon" />
                </div>
                <h3 className="bus-name">{bus.busName}</h3>
                <p className="bus-type">
                  {bus.busType?.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (c) => c.toUpperCase()) || "Unknown Type"}
                </p>
                <Link to={`/operator/bus/details/${bus.id}`} className="view-details-btn">
                  View Details
                </Link>
              </div>
            </div>
          )) : (
            <div className="no-buses-message">
              <p>No buses found in your fleet yet.</p>
            </div>
          )}
        </div>
      </div>



      {/* 3. Stats Section */}
      <div className="dashboard-section stats-section">
        <h2 className="section-title">Quick Stats</h2>
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon-wrapper">
              <FaBusAlt className="stat-icon" />
            </div>
            <div className="stat-content">
              <h3 className="stat-value">{buses.length}</h3>
              <p className="stat-label">Total Buses</p>
            </div>
          </div>
          <div className="stat-card">
            <div className="stat-icon-wrapper">
              <FaChartBar className="stat-icon" />
            </div>
            <div className="stat-content">
              <h3 className="stat-value">12</h3>
              <p className="stat-label">Active Routes</p>
            </div>
          </div>
          <div className="stat-card">
            <div className="stat-icon-wrapper">
              <FaClipboardList className="stat-icon" />
            </div>
            <div className="stat-content">
              <h3 className="stat-value">524</h3>
              <p className="stat-label">Monthly Bookings</p>
            </div>
          </div>
        </div>
      </div>

      {/* 4. Features Section */}
      <div className="dashboard-section features-section">
        <h2 className="section-title">Why Choose FastX</h2>
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">🚌</div>
            <h3 className="feature-title">Fleet Management</h3>
            <p className="feature-description">Easily manage your entire fleet from one dashboard</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">📊</div>
            <h3 className="feature-title">Real-time Analytics</h3>
            <p className="feature-description">Get insights into bookings and revenue</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🛠️</div>
            <h3 className="feature-title">24/7 Support</h3>
            <p className="feature-description">Dedicated support team for operators</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">💺</div>
            <h3 className="feature-title">Seat Management</h3>
            <p className="feature-description">Customizable seat layouts for each bus</p>
          </div>
        </div>
      </div>

      {/* 5. Footer */}
      <footer className="dashboard-footer">
        <div className="footer-container">
          {/* Left Side - Logo and Brand */}
          <div className="footer-brand">
            <img src="/images/fxlogo.png" alt="FastX Logo" className="footer-logo" />
            <span className="brand-name">FastX</span>
            <p className="brand-tagline">Travel makes easy</p>
          </div>

          {/* Right Side - Three Columns */}
          <div className="footer-columns">
            {/* Useful Links Column */}
            <div className="footer-column">
              <h4 className="column-title">Useful Links</h4>
              <ul className="footer-links" style={{
                opacity: 1,
                visibility: 'visible',
                display: 'block',
                color: 'red',
                fontSize: '20px',
                position: 'relative',
                zIndex: 9999,
                paddingTop: '30px'
              }}>
                <li><Link to="/operator">Home</Link></li>
                <li><Link to="/operator/buses">Fleet Management</Link></li>
                <li><Link to="/operator/routes">Route Planning</Link></li>
                <li><Link to="/operator/bookings">Bookings</Link></li>
              </ul>
            </div>

            {/* Contact Us Column */}
            <div className="footer-column">
              <h4 className="column-title">Contact Us</h4>
              <ul className="footer-contacts">
                <li>support@fastx.com</li>
                <li>+91 9876543210</li>
                <li>24/7 Customer Support</li>
              </ul>
            </div>

            {/* Address Column */}
            <div className="footer-column">
              <h4 className="column-title">Address</h4>
              <address>
                FastX Headquarters<br />
                123 Business Park<br />
                Bangalore, India 560001
              </address>
            </div>
          </div>
        </div>



        {/* Bottom Footer */}
        <div className="footer-bottom">
          <div className="copyright">
            &copy; {new Date().getFullYear()} FastX. All Rights Reserved
          </div>
          {/* Social Media Links */}
          <div className="footer-social">
            <a href="#" className="social-link"><FaFacebook /></a>
            <a href="#" className="social-link"><FaTwitter /></a>
            <a href="#" className="social-link"><FaInstagram /></a>
            <a href="#" className="social-link"><FaLinkedin /></a>
          </div>
          <div className="legal-links">
            <a href="#">Terms</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Support</a>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default Dashboard;