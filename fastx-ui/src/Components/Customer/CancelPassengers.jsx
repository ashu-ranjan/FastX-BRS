import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Customer/css/CancelPassengers.css';

const CancelPassengers = ({ booking, passengers, onComplete }) => {
    const [selectedPassengers, setSelectedPassengers] = useState([]);
    const [reason, setReason] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const togglePassenger = (passengerId) => {
        setSelectedPassengers(prev =>
            prev.includes(passengerId)
                ? prev.filter(id => id !== passengerId)
                : [...prev, passengerId]
        );
    };

    const handleBack = () => {
        window.location.reload(); // Full page refresh instead of navigation
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (selectedPassengers.length === 0) {
            setError('Please select at least one passenger');
            return;
        }

        setLoading(true);

        try {
            // Process each selected passenger
            for (const passengerId of selectedPassengers) {
                await axios.post(
                    `http://localhost:8080/fastx/api/cancellation/request/${passengerId}`,
                    { reason },
                    {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        }
                    }
                );
            }

            alert('Cancellation request submitted successfully!');
            onComplete(); // Refresh parent data
            window.location.reload();
        } catch (err) {
            setError(err.response?.data?.message || 'Partial cancellation approved.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="cancel-modal">
            <div className="cancel-content">
                <h2>Cancel Passengers - Booking #{booking.id}</h2>

                <div className="passenger-selection">
                    <h3>Select passengers to cancel:</h3>
                    <div className="passenger-list">
                        {passengers.map(passenger => (
                            <div key={passenger.id} className={`passenger-item ${passenger.isCancelled ? 'cancelled' : ''}`}>
                                <label>
                                    <input
                                        type="checkbox"
                                        checked={selectedPassengers.includes(passenger.id)}
                                        onChange={() => togglePassenger(passenger.id)}
                                        disabled={passenger.isCancelled}
                                    />
                                    <div className="passenger-info">
                                        <span className="name">{passenger.passenger}</span>
                                        <span className="seat">Seat: {passenger.seat?.seatNumber || 'N/A'}</span>
                                        {passenger.isCancelled && <span className="status-badge">Already Cancelled</span>}
                                    </div>
                                </label>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="reason-section">
                    <h3>Cancellation Reason:</h3>
                    <textarea
                        value={reason}
                        onChange={(e) => setReason(e.target.value)}
                        placeholder="Please specify your reason for cancellation"
                        required
                    />
                </div>

                {error && <div className="error-message">{error}</div>}

                <div className="modal-buttons">
                    <button
                        type="button"
                        className="back-btn"
                        onClick={handleBack}
                    >
                        Back
                    </button>
                    <button
                        type="submit"
                        className="submit-btn"
                        onClick={handleSubmit}
                        disabled={loading}
                    >
                        {loading ? 'Submitting...' : 'Submit Cancellation'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CancelPassengers;