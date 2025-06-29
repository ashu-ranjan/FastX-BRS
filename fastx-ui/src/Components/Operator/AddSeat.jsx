import axios from "axios";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import '../Operator/css/AddSeat.css'; // Include this
import { FaChair } from "react-icons/fa";

function AddSeat() {
  const { busId } = useParams();
  const navigate = useNavigate();

  const [seatNumber, setSeatNumber] = useState("");
  const [seatType, setSeatType] = useState("WINDOW");
  const [seatDeck, setSeatDeck] = useState("LOWER");
  const [price, setPrice] = useState("");
  const [isActive, setIsActive] = useState(true);

  const addSeat = async (e) => {
    e.preventDefault();
    try {
      await axios.post(
        `http://localhost:8080/fastx/api/add/seat/${busId}`,
        {
          seatNumber,
          seatType,
          seatDeck,
          price,
          isActive,
        },
        {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      );
      alert("Seat added successfully!");
      setSeatNumber("");
      setSeatType("");
      setSeatDeck("");
      setPrice("");
      setIsActive(true);
    } catch (error) {
      console.error("Error adding seat:", error);
      alert("Failed to add seat.");
    }
  };

  return (
    <div className="add-seat-page">
      <div className="add-seat-container">
        <h3 className="mb-4 d-flex align-items-center gap-2">
          <FaChair className="text-success" size={24} /> Add Seat
        </h3>
        <form onSubmit={addSeat}>
          <div className="mb-3">
            <label className="form-label">Seat Number</label>
            <input
              type="text"
              className="form-control"
              value={seatNumber}
              onChange={(e) => setSeatNumber(e.target.value)}
              placeholder="e.g., 1L, 2U"
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Seat Type</label>
            <select
              className="form-select"
              value={seatType}
              onChange={(e) => setSeatType(e.target.value)}
            >
              <option value="WINDOW">Window</option>
              <option value="MIDDLE">Middle</option>
              <option value="AISLE">Aisle</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Seat Deck</label>
            <select
              className="form-select"
              value={seatDeck}
              onChange={(e) => setSeatDeck(e.target.value)}
            >
              <option value="LOWER">Lower Deck</option>
              <option value="UPPER">Upper Deck</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Price (₹)</label>
            <input
              type="number"
              className="form-control"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              required
            />
          </div>

          <div className="form-check form-switch mb-3">
            <input
              className="form-check-input"
              type="checkbox"
              id="isActive"
              checked={isActive}
              onChange={() => setIsActive(!isActive)}
            />
            <label className="form-check-label" htmlFor="isActive">
              &nbsp; Available
            </label>
          </div>

          <button type="submit" className="btn btn-primary w-100">
            Add Seat
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddSeat;
