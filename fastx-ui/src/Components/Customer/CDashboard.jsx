import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import CustomerNavbar from './CustomerNavbar';
import '../Customer/css/CDashboard.css';
import { FaFacebook, FaInstagram, FaLinkedin, FaTwitter } from 'react-icons/fa';

function CDashboard() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useState({
    origin: '',
    destination: '',
    date: new Date().toISOString().split('T')[0]
  });

  const handleSearch = (e) => {
    e.preventDefault();
    if (!searchParams.origin || !searchParams.destination) {
      alert('Please enter origin and destination');
      return;
    }
    navigate(`/customer/buses?origin=${searchParams.origin}&destination=${searchParams.destination}&date=${searchParams.date}`);
  };

  const popularDestinations = [
    { id: 1, name: 'Delhi', image: '/images/delhi.jpg' },
    { id: 2, name: 'Mumbai', image: '/images/mumbai.jpg' },
    { id: 3, name: 'Bangalore', image: '/images/bengaluru.jpg' },
    { id: 4, name: 'Hyderabad', image: '/images/hyderabad.jpg' },
  ];

  const offers = [
    { id: 1, title: 'Get $100 off', code: 'FESTIVE', valid: 'Until 31 Aug 2025' },
    { id: 2, title: 'Student Discount', code: 'STUDENT25', valid: 'Valid with ID' },
    { id: 3, title: 'Weekend Special', code: 'WEEKEND20', valid: 'Fri-Sun only' },
  ];

  return (
    <div className="customer-dashboard">
      <CustomerNavbar />
      
      {/* Hero Section with Search */}
      <section className="hero-section">
        <div className="container">
          <div className="hero-content">
            <h1>India's No. 1 online bus ticket booking site</h1>
            <form className="search-box" onSubmit={handleSearch}>
              <div className="search-row">
                <div className="form-group">
                  <label>From</label>
                  <input 
                    type="text" 
                    placeholder="Enter origin city" 
                    value={searchParams.origin}
                    onChange={(e) => setSearchParams({...searchParams, origin: e.target.value})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>To</label>
                  <input 
                    type="text" 
                    placeholder="Enter destination city" 
                    value={searchParams.destination}
                    onChange={(e) => setSearchParams({...searchParams, destination: e.target.value})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Date of Journey</label>
                  <input 
                    type="date" 
                    value={searchParams.date}
                    onChange={(e) => setSearchParams({...searchParams, date: e.target.value})}
                  />
                </div>
              </div>
              <div className="search-actions">

                <button type="submit" className="search-btn" style={{ marginLeft: '220px' }}>Search buses</button>
              </div>
            </form>
          </div>
        </div>
      </section>

      {/* offer Booking Banner */}
      <section className="train-banner">
        <div className="container">
          <div className="banner-content">
            <div className="offer-badge">Get $100 off using code FESTIVE</div>
            <div className="month-tabs">
              <span className="active">Jul</span>
              <span>Aug</span>
            </div>
          </div>
        </div>
      </section>

      {/* Offers Section */}
      <section className="offers-section">
        <div className="container">
          <h2 className="section-title">Special Offers</h2>
          <div className="offers-grid">
            {offers.map(offer => (
              <div key={offer.id} className="offer-card">
                <h3>{offer.title}</h3>
                <p className="code">{offer.code}</p>
                <p className="valid">{offer.valid}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Popular Destinations */}
      <section className="destinations-section">
        <div className="container">
          <h2 className="section-title">Popular Destinations</h2>
          <div className="destinations-grid">
            {popularDestinations.map(destination => (
              <div key={destination.id} className="destination-card">
                <img src={destination.image} alt={destination.name} />
                <div className="overlay"></div>
                <h3>{destination.name}</h3>
              </div>
            ))}
          </div>
        </div>
      </section>
      

      {/* New Footer Implementation */}
      <footer className="dashboard-footer">
        <div className="footer-container">
          {/* Left Side - Logo and Brand */}
          <div className="footer-brand">
            <img src="/images/fxlogo.png" alt="FastX Logo" className="footer-logo" />
            <span className="brand-name">FastX</span>
            <p className="brand-tagline">India's No. 1 online bus ticket booking site</p>
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
                <li><Link to="/customer">Home</Link></li>
                <li><Link to="/customer/bookings">My Bookings</Link></li>
                <li><Link to="/customer/routes">Browse Routes</Link></li>
                <li><Link to="/customer/help">Help</Link></li>
              </ul>
            </div>

            {/* Services Column */}
            <div className="footer-column">
              <h4 className="column-title">Services</h4>
              <ul className="footer-services" >
                <li><Link to="#">Bus Tickets</Link></li>
                <li><Link to="#">Train Tickets</Link></li>
                <li><Link to="#">Hotels</Link></li>
                <li><Link to="#">Cab Services</Link></li>
              </ul>
            </div>

            {/* Contact Column */}
            <div className="footer-column">
              <h4 className="column-title">Contact Us</h4>
              <ul className="footer-contacts">
                <li>info@ticket.com</li>
                <li>+91 1234567890</li>
                <li>24/7 Customer Support</li>
                <li>India - 560001</li>
              </ul>
            </div>
          </div>
        </div>

        {/* Bottom Footer */}
        <div className="footer-bottom">
          <div className="copyright">
            &copy; {new Date().getFullYear()} FastX. All Rights Reserved
          </div>
          <div className="footer-social">
            <Link to="" className="social-link"><FaFacebook /></Link>
            <Link to="" className="social-link"><FaTwitter /></Link>
            <Link to="" className="social-link"><FaInstagram /></Link>
            <Link to="" className="social-link"><FaLinkedin /></Link>
          </div>
          <div className="legal-links">
            <Link to="">Terms</Link>
            <Link to="">Privacy Policy</Link>
            <Link to="">Support</Link>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default CDashboard;