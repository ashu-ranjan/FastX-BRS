import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import '../Operator/css/SeatLayout.css';
import { FaChair } from 'react-icons/fa';          // For Seater icon
import { FaBusSimple } from 'react-icons/fa6';     // For heading icon

function SeatLayout() {
  const { busId } = useParams();
  const [seats, setSeats] = useState([]);
  const [busType, setBusType] = useState('');

  useEffect(() => {
    // Fetch seat data and normalize active status
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
          setBusType(normalizedSeats[0].bus.busType); // Set bus type (SEATER, SLEEPER, etc.)
        }
      } catch (error) {
        console.error("Failed to fetch seats", error);
      }
    };

    if (busId) getSeats();
  }, [busId]);

  const lowerSeats = seats.filter(seat => seat.seatDeck === 'LOWER');
  const upperSeats = seats.filter(seat => seat.seatDeck === 'UPPER');

  // Render single seat with appropriate icon based on bus type
  const renderSeat = (seat) => {
    const isActive =
      seat.isActive === 1 ||
      seat.isActive === true ||
      seat.isActive === '\u0001' ||
      seat.isActive?.data?.[0] === 1;

    const isSleeper = busType.includes('SLEEPER') && !busType.includes('SEMI');

    return (
      <div
        key={seat.id}
        className={`seat-box ${isActive ? 'active' : 'inactive'}`}
      >
        {/* Icon section: use bed icon for sleeper, chair icon for others */}
        <div className="seat-icon">
          {isSleeper ? (
            <i className="pi pi-bed"></i>
          ) : (
            <FaChair />
          )}
        </div>

        {/* Seat number and price */}
        <div className="seat-number">{seat.seatNumber}</div>
        <div className="seat-price">₹{seat.price}</div>
      </div>
    );
  };

  // Generate seat layout grid based on deck and bus type
  const layout = () => {
    const formatRow = (deckSeats, layoutType) => {
  const rows = [];

  if (layoutType === 'SLEEPER') {
    for (let i = 0; i < deckSeats.length; i += 3) {
      rows.push(
        <div className="sleeper-row" key={i}>
          <div className="seat-single">{deckSeats[i] && renderSeat(deckSeats[i])}</div>
          <div className="seat-pair">
            {deckSeats[i + 1] ? renderSeat(deckSeats[i + 1]) : <div className="seat-placeholder" />}
            {deckSeats[i + 2] ? renderSeat(deckSeats[i + 2]) : <div className="seat-placeholder" />}
          </div>
        </div>
      );
    }
  } else {
    for (let i = 0; i < deckSeats.length; i += 4) {
      rows.push(
        <div className="semi-row" key={i}>
          <div className="seat-pair">
            {deckSeats[i] ? renderSeat(deckSeats[i]) : <div className="seat-placeholder" />}
            {deckSeats[i + 1] ? renderSeat(deckSeats[i + 1]) : <div className="seat-placeholder" />}
          </div>
          <div className="seat-space"></div>
          <div className="seat-pair">
            {deckSeats[i + 2] ? renderSeat(deckSeats[i + 2]) : <div className="seat-placeholder" />}
            {deckSeats[i + 3] ? renderSeat(deckSeats[i + 3]) : <div className="seat-placeholder" />}
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
      <div className="deck-wrapper">
        {/* Lower Deck Section */}
        <div className="deck-box">
          <div className="deck-title">Lower Deck</div>
          {formatRow(lowerSeats, isSleeper ? 'SLEEPER' : 'SEMI')}
        </div>

        {/* Upper Deck only for sleeper/non-seater */}
        {!isSeater && (
          <div className="deck-box">
            <div className="deck-title">Upper Deck</div>
            {formatRow(upperSeats, 'SLEEPER')}
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="seat-layout-outer-box">
      <div className="seat-layout-container">
        {/* Heading */}
        <h2 className="seat-layout-title">
          <FaBusSimple className="text-success" size={40} /> Seat Layout
        </h2>

        {/* Render layout based on type */}
        {layout()}

        {/* Action buttons */}
        <div className="seat-actions">
          <Link className="btn btn-success" to={`/operator/bus/details/${busId}/add-seat`}>
            <i className="pi pi-plus"></i> Add Seats
          </Link>
          <Link className="btn btn-primary" to={`/operator/bus/details/${busId}/manage-seat`}>
            <i className="pi pi-cog"></i> Manage Seats
          </Link>
        </div>
      </div>
    </div>
  );
}

export default SeatLayout;
