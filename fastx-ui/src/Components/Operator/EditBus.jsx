import { useEffect, useState } from "react";
import { FaTimes } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { FaBus, FaSave } from "react-icons/fa";
import '../Operator/css/EditBus.css'; // Assuming you have a CSS file for styling

function EditBus() {

    const { busId } = useParams();
    const navigate = useNavigate();
    const [busName, setBusName] = useState('');
    const [busNumber, setBusNumber] = useState('');
    const [busClass, setBusClass] = useState('');
    const [busType, setBusType] = useState('');
    const [capacity, setCapacity] = useState('');
    const [isActive, setIsActive] = useState(false);

    useEffect(() => {
        const getBusDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/fastx/api/bus/get-bus/${busId}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                const busData = response.data;
                setBusName(busData.busName);
                setBusNumber(busData.busNumber);
                setBusClass(busData.busClass);
                setBusType(busData.busType);
                setCapacity(busData.capacity);
                setIsActive(busData.isActive);
            } catch (error) {
                console.error("Error fetching bus details:", error);
            }
        };
        getBusDetails();
    }, [busId])

    const updateBusDetails = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:8080/fastx/api/bus/update/${busId}`, {
                busName,
                busNumber,
                busClass,
                busType,
                capacity,
                isActive
            }, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            alert('Bus details updated successfully');
            navigate(-1); // Navigate back to the previous page
        } catch (error) {
            console.error("Error updating bus details:", error);
            alert('Failed to update bus details');
        }
    };

  return (
        <div className="edit-bus-container">
            <div className="edit-bus-card">
                <div className="card-header">
                    <h2><FaBus /> Edit Bus Details</h2>
                </div>

                <form onSubmit={updateBusDetails} className="edit-bus-form">
                    <div className="form-group">
                        <label>Bus Name</label>
                        <input
                            type="text"
                            name="busName"
                            value={busName}
                            onChange={($e) => setBusName($e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Bus Number</label>
                        <input
                            type="text"
                            name="busNumber"
                            value={busNumber}
                            onChange={($e) => setBusNumber($e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Bus Class</label>
                        <select
                            name="busClass"
                            value={busClass}
                            onChange={($e) => setBusClass($e.target.value)}
                            required
                        >
                            <option value="">Select Class</option>
                            <option value="LUXURY">Luxury</option>
                            <option value="STANDARD">Standard</option>
                            <option value="ECONOMY">Economy</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Bus Type</label>
                        <select
                            name="busType"
                            value={busType}
                            onChange={($e) => setBusType($e.target.value)}
                            required
                        >
                            <option value="">Select Type</option>
                            <option value="AC_SLEEPER">Sleeper-AC</option>
                            <option value="NON_AC_SLEEPER">Sleeper</option>
                            <option value="AC_SEMI_SLEEPER">Semi-Sleeper-AC</option>
                            <option value="NON_AC_SEMI_SLEEPER">Semi-Sleeper</option>
                            <option value="AC_SEATER">Seater-AC</option>
                            <option value="NON_AC_SEATER">Seater</option>

                        </select>
                    </div>

                    <div className="form-group">
                        <label>Capacity</label>
                        <input
                            type="number"
                            name="capacity"
                            value={capacity}
                            onChange={($e) => setCapacity($e.target.value)}
                            min="1"
                            required
                        />
                    </div>

                    <div className="form-group checkbox-group">
                        <label>
                            <input
                                type="checkbox"
                                name="isActive"
                                checked={isActive}
                                onChange={($e) => setIsActive($e.target.checked)}
                            />
                            Active
                        </label>
                    </div>

                    <div className="form-actions">
                        <button
                            type="button"
                            className="cancel-btn"
                            onClick={() => navigate(-1)}
                        >
                            <FaTimes /> Cancel
                        </button>
                        <button
                            type="submit"
                            className="save-btn"
                        >
                            <FaSave /> Save Changes
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default EditBus