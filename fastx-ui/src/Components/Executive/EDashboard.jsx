import axios from 'axios';
import { useEffect, useState } from 'react';
import { FaClipboardList, FaCheck, FaTimes, FaChartBar, FaFacebook, FaInstagram, FaLinkedin, FaTwitter } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import '../Executive/css/EDashboard.css';

function EDashboard() {
  const [cancellations, setCancellations] = useState([]);
  const name = localStorage.getItem("name") || "Executive";

  useEffect(() => {
    const fetchCancellations = async () => {
      try {
        const response = await axios.get("http://localhost:8080/fastx/api/cancellation/get-all/pending", {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
        });
        setCancellations(response.data);
      } catch (error) {
        console.error("Error fetching cancellations:", error);
      }
    };
    fetchCancellations();
  }, []);

  const handleApproval = async (cancellationId, status) => {
    try {
      await axios.put("http://localhost:8080/fastx/api/cancellation/approval", {
        cancellationId,
        refundStatus: status,
        remarks: status === 'APPROVED' ? 'Approved by executive' : 'Rejected by executive'
      }, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      });
      
      // Refresh the list after approval
      const response = await axios.get("http://localhost:8080/fastx/api/cancellation/get-all/pending", {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      });
      setCancellations(response.data);
    } catch (error) {
      console.error("Error processing cancellation:", error);
    }
  };

  return (
    <div className="executive-dashboard-container">
      {/* Header Section */}
      <div className="executive-welcome-section">
        <div className="executive-welcome-content">
          <h1 className="executive-heading-title">
            <span className="executive-heading-icon-wrapper">
              <FaClipboardList className="executive-heading-icon" />
            </span>
            Welcome, <span className="executive-highlight-name">{name}</span>
          </h1>
          <p className="executive-welcome-subtitle">Manage cancellation requests and refund approvals.</p>
        </div>
      </div>

      {/* Cancellations List */}
      <div className="executive-dashboard-section">
        <h2 className="executive-section-title">Pending Cancellations</h2>
        <div className="executive-cancellations-grid">
          {cancellations.length > 0 ? (
            <table className="executive-cancellations-table">
              <thead>
                <tr>
                  <th>Booking ID</th>
                  <th>Customer</th>
                  <th>Bus Details</th>
                  <th>Reason</th>
                  <th>Request Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {cancellations.map((request) => (
                  <tr key={request.id} className="executive-cancellation-row">
                    <td>{request.bookingId || 'N/A'}</td>
                    <td>{request.customerName || 'Unknown'}</td>
                    <td>
                      {request.busName && (
                        <div>
                          <strong>{request.busName}</strong><br />
                          {request.busType?.replace(/_/g, ' ')}
                        </div>
                      )}
                    </td>
                    <td>{request.reason || 'No reason provided'}</td>
                    <td>{request.requestDate ? new Date(request.requestDate).toLocaleDateString() : 'N/A'}</td>
                    <td className="executive-actions-cell">
                      <button 
                        className="executive-approve-btn"
                        onClick={() => handleApproval(request.id, 'APPROVED')}
                      >
                        <FaCheck /> Approve
                      </button>
                      <button 
                        className="executive-reject-btn"
                        onClick={() => handleApproval(request.id, 'REJECTED')}
                      >
                        <FaTimes /> Reject
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <div className="executive-no-requests-message">
              <p>No pending cancellation requests found.</p>
            </div>
          )}
        </div>
      </div>

      {/* Stats Section */}
      <div className="executive-dashboard-section executive-stats-section">
        <h2 className="executive-section-title">Quick Stats</h2>
        <div className="executive-stats-grid">
          <div className="executive-stat-card">
            <div className="executive-stat-icon-wrapper">
              <FaClipboardList className="executive-stat-icon" />
            </div>
            <div className="executive-stat-content">
              <h3 className="executive-stat-value">{cancellations.length}</h3>
              <p className="executive-stat-label">Pending Requests</p>
            </div>
          </div>
          <div className="executive-stat-card">
            <div className="executive-stat-icon-wrapper">
              <FaChartBar className="executive-stat-icon" />
            </div>
            <div className="executive-stat-content">
              <h3 className="executive-stat-value">0</h3>
              <p className="executive-stat-label">Processed Today</p>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="executive-dashboard-footer">
        <div className="executive-footer-container">
          <div className="executive-footer-brand">
            <span className="executive-brand-name">FastX</span>
            <p className="executive-brand-tagline">Travel made easy</p>
          </div>

          <div className="executive-footer-columns">
            <div className="executive-footer-column">
              <h4 className="executive-column-title">Useful Links</h4>
              <ul className="executive-footer-links">
                <li><Link to="/executive">Dashboard</Link></li>
                <li><Link to="/executive/cancellations">Cancellations</Link></li>
              </ul>
            </div>

            <div className="executive-footer-column">
              <h4 className="executive-column-title">Contact Us</h4>
              <ul className="executive-footer-contacts">
                <li>support@fastx.com</li>
                <li>+91 9876543210</li>
              </ul>
            </div>
          </div>
        </div>

        <div className="executive-footer-bottom">
          <div className="executive-copyright">
            &copy; {new Date().getFullYear()} FastX. All Rights Reserved
          </div>
          <div className="executive-footer-social">
            <Link to="#" className="executive-social-link"><FaFacebook /></Link>
            <Link to="#" className="executive-social-link"><FaTwitter /></Link>
            <Link to="#" className="executive-social-link"><FaInstagram /></Link>
            <Link to="#" className="executive-social-link"><FaLinkedin /></Link>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default EDashboard;