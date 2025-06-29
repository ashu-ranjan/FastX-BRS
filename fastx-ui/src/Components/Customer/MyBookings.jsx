import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { QRCodeSVG as QRCode } from 'qrcode.react';
import '../Customer/css/MyBookings.css';
import { FaArrowRight } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import CancelPassengers from './CancelPassengers';

const MyBookings = () => {
    const [bookings, setBookings] = useState([]);
    const [passengers, setPassengers] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(5);
    const [totalPages, setTotalPages] = useState(0);
    const [showCancelModal, setShowCancelModal] = useState(false);
    const [selectedBooking, setSelectedBooking] = useState(null);
    const navigate = useNavigate();

    const fetchBookings = async () => {
        try {
            setLoading(true);
            const response = await axios.get(
                `http://localhost:8080/fastx/api/booking/history?page=${page}&size=${size}`,
                { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } }
            );
            
            setBookings(response.data.content);
            setTotalPages(response.data.totalPages);

            const passengerData = {};
            for (const booking of response.data.content) {
                try {
                    const passengerResponse = await axios.get(
                        `http://localhost:8080/fastx/api/booking-details/by-bookingId/${booking.id}`,
                        { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } }
                    );
                    passengerData[booking.id] = passengerResponse.data;
                } catch (err) {
                    passengerData[booking.id] = [];
                }
            }
            setPassengers(passengerData);
            setLoading(false);
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to fetch bookings');
            setLoading(false);
        }
    };

    useEffect(() => { fetchBookings(); }, [page, size]);

    const handleCancelClick = (booking) => {
        setSelectedBooking(booking);
        setShowCancelModal(true);
    };

    if (loading) return <div className="loading">Loading your bookings...</div>;
    if (error) return <div className="error">{error}</div>;
    if (bookings.length === 0) return <div className="no-bookings">No bookings found</div>;

    return (
        <div className="bookings-container">
            <h1>My Booking History</h1>

            <div className="pagination-controls">
                <button onClick={() => setPage(p => Math.max(p - 1, 0))} disabled={page === 0}>
                    Previous
                </button>
                <span>Page {page + 1} of {totalPages}</span>
                <button onClick={() => setPage(p => Math.min(p + 1, totalPages - 1))} disabled={page >= totalPages - 1}>
                    Next
                </button>
                <select value={size} onChange={(e) => setSize(Number(e.target.value))}>
                    <option value="5">5 per page</option>
                    <option value="10">10 per page</option>
                    <option value="20">20 per page</option>
                </select>
            </div>

            <div className="bookings-list">
                {bookings.map((booking) => (
                    <div key={booking.id} className="booking-ticket">
                        <div className="ticket-header">
                            <h2>Booking #{booking.id}</h2>
                            <span className={`status ${booking.status.toLowerCase()}`}>
                                {booking.status}
                            </span>
                        </div>
                        
                        <div className="ticket-body">
                            <div className="ticket-section">
                                <h3>Journey Details</h3>
                                <p><strong>Date:</strong> {new Date(booking.journeyDate).toLocaleDateString()}</p>
                                <p><strong>Booked On:</strong> {new Date(booking.bookedOn).toLocaleString()}</p>
                                <p><strong>Total Seats:</strong> {booking.totalSeat}</p>
                                <p><strong>Total Fare:</strong> ₹{booking.totalFare?.toFixed(2)}</p>
                                
                                {booking.schedule && (
                                    <>
                                        <p><strong>Bus:</strong> {booking.schedule.bus?.busName} ({booking.schedule.bus?.busNumber})</p>
                                        <p><strong>Route:</strong> {booking.schedule.busRoute?.origin} <FaArrowRight /> {booking.schedule.busRoute?.destination}</p>
                                        <p><strong>Departure:</strong> {booking.schedule.departureTime}</p>
                                    </>
                                )}
                            </div>
                            
                            <div className="ticket-section">
                                <h3>Passenger Details</h3>
                                {passengers[booking.id]?.map((passenger) => (
                                    <div key={passenger.id} className="passenger-detail">
                                        <div className="passenger-row">
                                            <p><strong>Name:</strong> {passenger.passenger}</p>
                                            <p><strong>Age:</strong> {passenger.age}</p>
                                        </div>
                                        <div className="passenger-row">
                                            <p><strong>Gender:</strong> {passenger.gender}</p>
                                            <p><strong>Seat:</strong> {passenger.seat?.seatNumber || 'N/A'}</p>
                                        </div>
                                        {passenger.isCancelled ? (
                                            <p className="cancelled-badge">Cancelled</p>
                                        ) : passenger.cancellationRequested ? (
                                            <p className="pending-badge">Cancellation Requested</p>
                                        ) : null}
                                    </div>
                                ))}
                            </div>
                            
                            <div className="ticket-section qr-code">
                                <QRCode 
                                    value={`BookingID:${booking.id}`}
                                    size={128}
                                    level="H"
                                />
                                <p className="qr-note">Scan at boarding</p>
                            </div>
                        </div>
                        
                        <div className="ticket-footer">
                            {booking.status === 'CONFIRMED' && (
                                <button 
                                    className="cancel-btn"
                                    onClick={() => handleCancelClick(booking)}
                                >
                                    Cancel Passengers
                                </button>
                            )}
                            <p>Thank you for traveling with us!</p>
                        </div>
                    </div>
                ))}
            </div>

            {showCancelModal && selectedBooking && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <CancelPassengers
                            booking={selectedBooking}
                            passengers={passengers[selectedBooking.id] || []}
                            onCancelComplete={() => {
                                setShowCancelModal(false);
                                fetchBookings();
                            }}
                        />
                    </div>
                </div>
            )}
        </div>
    );
};

export default MyBookings;