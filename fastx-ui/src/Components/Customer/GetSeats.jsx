import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import '../Customer/css/GetSeats.css';
import { FaChair, FaCheckCircle } from 'react-icons/fa';
import { FaBusSimple } from 'react-icons/fa6';
import { Modal, Button, Form, Alert } from 'react-bootstrap';

function GetSeats() {
  const { busId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { scheduleId, journeyDate, origin, destination } = location.state || {};
  
  const [seats, setSeats] = useState([]);
  const [busType, setBusType] = useState('');
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [showPassengerForm, setShowPassengerForm] = useState(false);
  const [currentSeat, setCurrentSeat] = useState(null);
  const [passengerDetails, setPassengerDetails] = useState({
    passenger: '',
    age: '',
    gender: 'MALE',
    boardingPoint: origin || '',
    droppingPoint: destination || ''
  });
  const [isBooking, setIsBooking] = useState(false);
  const [error, setError] = useState('');
  const [bookingSuccess, setBookingSuccess] = useState(false);
  const [bookingId, setBookingId] = useState(null);
  const [totalAmount, setTotalAmount] = useState(0);

  useEffect(() => {
    if (!scheduleId || !journeyDate) {
      setError('Schedule information missing. Please select a bus again.');
      navigate('/customer');
      return;
    }

    const getSeats = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/fastx/api/get/seats/bus/${busId}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        });
        const normalizedSeats = response.data.map(seat => ({
          ...seat,
          isActive: seat.active === 1 || seat.active === true || seat.active === '\u0001'
        }));
        setSeats(normalizedSeats);
        if (normalizedSeats.length > 0) {
          setBusType(normalizedSeats[0].bus.busType);
        }
      } catch (error) {
        console.error("Failed to fetch seats", error);
        setError('Failed to load seat information. Please try again.');
      }
    };

    if (busId) getSeats();
  }, [busId, navigate, scheduleId, journeyDate]);

  useEffect(() => {
    // Calculate total amount whenever selected seats change
    const amount = selectedSeats.reduce((sum, seat) => sum + seat.price, 0);
    setTotalAmount(amount);
  }, [selectedSeats]);

  const handleSeatClick = (seat) => {
    if (!seat.isActive) return;
    
    const isSelected = selectedSeats.some(s => s.id === seat.id);
    
    if (isSelected) {
      setSelectedSeats(selectedSeats.filter(s => s.id !== seat.id));
    } else {
      setCurrentSeat(seat);
      setShowPassengerForm(true);
    }
  };

  const handlePassengerFormSubmit = (e) => {
    e.preventDefault();
    
    const newPassenger = {
      seatId: currentSeat.id,
      passenger: passengerDetails.passenger,
      age: passengerDetails.age,
      gender: passengerDetails.gender,
      boardingPoint: passengerDetails.boardingPoint,
      droppingPoint: passengerDetails.droppingPoint
    };
    
    setSelectedSeats([...selectedSeats, {
      ...currentSeat,
      passenger: newPassenger,
      hasPassenger: true
    }]);
    
    setPassengerDetails({
      passenger: '',
      age: '',
      gender: 'MALE',
      boardingPoint: origin || '',
      droppingPoint: destination || ''
    });
    setShowPassengerForm(false);
  };

  const handleBooking = async () => {
    if (selectedSeats.length === 0) {
      setError('Please select at least one seat');
      return;
    }

    // Check if all selected seats have passenger info
    const seatsWithoutPassenger = selectedSeats.filter(seat => !seat.passenger);
    if (seatsWithoutPassenger.length > 0) {
      setError('Please fill passenger details for all selected seats');
      return;
    }

    const formattedJourneyDate = new Date(journeyDate).toISOString().split('T')[0];

    const bookingRequest = {
      scheduleId: scheduleId,
      journeyDate: formattedJourneyDate,
      passengers: selectedSeats.map(seat => ({
        seatId: seat.id,
        passenger: seat.passenger.passenger,
        age: seat.passenger.age,
        gender: seat.passenger.gender,
        boardingPoint: seat.passenger.boardingPoint,
        droppingPoint: seat.passenger.droppingPoint
      }))
    };

    try {
      setIsBooking(true);
      setError('');
      const response = await axios.post('http://localhost:8080/fastx/api/book', bookingRequest, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      
      setBookingId(response.data.id);
      setBookingSuccess(true);
      alert('Booking initiated successfully!');
    } catch (error) {
      console.error('Booking failed:', error);
      setError(error.response?.data?.message || 'Booking failed. Please try again.');
    } finally {
      setIsBooking(false);
    }
  };

  const handleProceedToPayment = () => {
    if (!bookingId) {
      setError('Please complete booking first');
      return;
    }
    navigate('/customer/payment', { 
      state: { 
        bookingId: bookingId,
        totalAmount: totalAmount,
        journeyDate: journeyDate,
        origin: origin,
        destination: destination,
        busType: busType,
        selectedSeats: selectedSeats.map(seat => ({
          seatNumber: seat.seatNumber,
          passenger: seat.passenger.passenger
        }))
      } 
    });
  };

  const renderSeat = (seat) => {
    const isActive = seat.isActive;
    const isSelected = selectedSeats.some(s => s.id === seat.id);
    const hasPassenger = selectedSeats.some(s => s.id === seat.id && s.passenger);
    const isSleeper = busType.includes('SLEEPER') && !busType.includes('SEMI');

    return (
      <div
        key={seat.id}
        className={`seat-container-uniq 
          ${isActive ? 'active-seat-uniq' : 'inactive-seat-uniq'}
          ${isSelected ? (hasPassenger ? 'confirmed-seat-uniq' : 'selected-seat-uniq') : ''}`}
        onClick={() => handleSeatClick(seat)}
      >
        <div className="seat-icon-uniq">
          {isSleeper ? (
            <i className="pi pi-bed"></i>
          ) : (
            <FaChair />
          )}
          {hasPassenger && <FaCheckCircle className="seat-confirmed-icon" />}
        </div>
        <div className="seat-number-uniq">{seat.seatNumber}</div>
        <div className="seat-price-uniq">₹{seat.price}</div>
      </div>
    );
  };

  const layout = () => {
    const formatRow = (deckSeats, layoutType) => {
      const rows = [];

      if (layoutType === 'SLEEPER') {
        for (let i = 0; i < deckSeats.length; i += 3) {
          rows.push(
            <div className="sleeper-row-uniq" key={i}>
              <div className="seat-single-uniq">{deckSeats[i] && renderSeat(deckSeats[i])}</div>
              <div className="seat-pair-uniq">
                {deckSeats[i + 1] ? renderSeat(deckSeats[i + 1]) : <div className="seat-placeholder-uniq" />}
                {deckSeats[i + 2] ? renderSeat(deckSeats[i + 2]) : <div className="seat-placeholder-uniq" />}
              </div>
            </div>
          );
        }
      } else {
        for (let i = 0; i < deckSeats.length; i += 4) {
          rows.push(
            <div className="semi-row-uniq" key={i}>
              <div className="seat-pair-uniq">
                {deckSeats[i] ? renderSeat(deckSeats[i]) : <div className="seat-placeholder-uniq" />}
                {deckSeats[i + 1] ? renderSeat(deckSeats[i + 1]) : <div className="seat-placeholder-uniq" />}
              </div>
              <div className="seat-space-uniq"></div>
              <div className="seat-pair-uniq">
                {deckSeats[i + 2] ? renderSeat(deckSeats[i + 2]) : <div className="seat-placeholder-uniq" />}
                {deckSeats[i + 3] ? renderSeat(deckSeats[i + 3]) : <div className="seat-placeholder-uniq" />}
              </div>
            </div>
          );
        }
      }
      return rows;
    };

    const isSeater = busType.includes('SEATER');
    const isSemi = busType.includes('SEMI');
    const isSleeper = busType.includes('SLEEPER') && !isSemi;

    return (
      <div className="deck-wrapper-uniq">
        <div className="deck-box-uniq">
          <div className="deck-title-uniq">Lower Deck</div>
          {formatRow(lowerSeats, isSleeper ? 'SLEEPER' : 'SEMI')}
        </div>
        {!isSeater && (
          <div className="deck-box-uniq">
            <div className="deck-title-uniq">Upper Deck</div>
            {formatRow(upperSeats, 'SLEEPER')}
          </div>
        )}
      </div>
    );
  };

  const lowerSeats = seats.filter(seat => seat.seatDeck === 'LOWER');
  const upperSeats = seats.filter(seat => seat.seatDeck === 'UPPER');

  return (
    <div className="seat-layout-outer-uniq">
      <div className="seat-layout-container-uniq">
        <h2 className="seat-layout-title-uniq">
          <FaBusSimple className="text-success" size={40} /> Seat Layout
        </h2>
        
        {error && <Alert variant="danger">{error}</Alert>}
        {bookingSuccess && <Alert variant="success">Booking confirmed! Ready for payment.</Alert>}
        
        {layout()}
        
        <div className="selected-seats-section">
          <div className="selected-seats-info">
            <h5>Selected Seats: {selectedSeats.length}</h5>
            <ul>
              {selectedSeats.map(seat => (
                <li key={seat.id} className={seat.passenger ? 'confirmed-passenger' : ''}>
                  {seat.seatNumber} - {seat.passenger?.passenger || 'No passenger info'}
                </li>
              ))}
            </ul>
            <div className="total-amount">
              <strong>Total Amount:</strong> ₹{totalAmount}
            </div>
          </div>
          
          <div className="booking-actions">
            <Button 
              variant="primary" 
              onClick={handleBooking}
              disabled={selectedSeats.length === 0 || isBooking || bookingSuccess}
            >
              {isBooking ? 'Processing...' : 'Confirm Booking'}
            </Button>
            
            <Button 
              variant="success" 
              onClick={handleProceedToPayment}
              disabled={!bookingSuccess}
              className="payment-btn"
            >
              Proceed to Payment
            </Button>
          </div>
        </div>
      </div>

      <Modal show={showPassengerForm} onHide={() => setShowPassengerForm(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Passenger Details for Seat {currentSeat?.seatNumber}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handlePassengerFormSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Passenger Name</Form.Label>
              <Form.Control 
                type="text" 
                value={passengerDetails.passenger}
                onChange={(e) => setPassengerDetails({
                  ...passengerDetails, 
                  passenger: e.target.value
                })}
                required
              />
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Age</Form.Label>
              <Form.Control 
                type="number" 
                value={passengerDetails.age}
                onChange={(e) => setPassengerDetails({...passengerDetails, age: e.target.value})}
                required
                min={1}
                max={100}
              />
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Gender</Form.Label>
              <Form.Select 
                value={passengerDetails.gender}
                onChange={(e) => setPassengerDetails({...passengerDetails, gender: e.target.value})}
                required
              >
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
              </Form.Select>
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Boarding Point</Form.Label>
              <Form.Control 
                type="text" 
                value={passengerDetails.boardingPoint}
                onChange={(e) => setPassengerDetails({...passengerDetails, boardingPoint: e.target.value})}
                required
              />
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Dropping Point</Form.Label>
              <Form.Control 
                type="text" 
                value={passengerDetails.droppingPoint}
                onChange={(e) => setPassengerDetails({...passengerDetails, droppingPoint: e.target.value})}
                required
              />
            </Form.Group>
            
            <Button variant="primary" type="submit">
              Save Passenger
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
}

export default GetSeats;