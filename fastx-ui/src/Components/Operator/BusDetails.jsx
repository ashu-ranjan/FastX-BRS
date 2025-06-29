import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link, useParams } from 'react-router-dom';
import { fetchBusAndDriverDetails } from './store/actions/DriverAction';
import { FaBus, FaUser, FaIdCard, FaChair, FaRoute, FaClock, FaWifi, FaEdit, FaCommentAlt, FaCheckCircle, FaAward, FaPlus, FaMailBulk, FaPhoneAlt, FaTrash, FaCalendarAlt } from 'react-icons/fa';
import '../Operator/css/BusDetails.css'; // Assuming you have a CSS file for styling
import axios from 'axios';

function BusDetails() {
    const { busId } = useParams();
    const [schedules, setSchedules] = useState("");
    const dispatch = useDispatch();
    const { bus, driver } = useSelector((state) => state.driver);

    useEffect(() => {
        dispatch(fetchBusAndDriverDetails(busId));
    }, [busId, dispatch]);



    const deleteDriver = async (driverId, busId) => {
    if (window.confirm("Are you sure you want to remove this driver?")) {
        try {
            await axios.delete(
                `http://localhost:8080/fastx/api/driver/delete/${driverId}/bus/${busId}`,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                }
            );
            setTimeout(() => {
        dispatch(fetchBusAndDriverDetails(busId));
      }, 500);
            alert('Driver removed successfully');
        } catch (error) {
            console.error("Error removing driver:", error);
            alert('Failed to remove driver');
        }
    }
};

    // Convert amenities string to array
    const amenities = bus.amenities ? bus.amenities.split(',') : [];

    return (
        <div className="container py-4">
            {/* Bus Details Card */}
            <div className="card shadow-sm mb-4">
                <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <h4 className="mb-0"><FaBus className="me-2" /> Bus Details</h4>
                    <Link
                        to={`/operator/edit-bus/${busId}`}
                        className="btn btn-sm btn-light"
                    >
                        <FaEdit className="me-1" /> Edit
                    </Link>
                </div>
                <div className="card-body">
                    <div className="row">
                        <div className="col-md-5">
                            <p><strong>Bus Name:</strong> {bus.busName || 'N/A'}</p>
                            <p><strong>Bus Number:</strong> {bus.busNumber || 'N/A'}</p>
                            <p><strong>Bus Class:</strong> {bus.busClass || 'N/A'}</p>
                        </div>
                        <div className="col-md-5">
                            <p><strong>Bus Type:</strong> {bus.busType || 'N/A'}</p>
                            <p><strong>Capacity:</strong> {bus.capacity || 'N/A'}</p>
                            <p><strong>Status:</strong>
                                <span className={`badge ${(bus.isActive !== false) ? 'bg-success' : 'bg-danger'}`}>
                                    {(bus.isActive !== false) ? 'Active' : 'Inactive'}
                                </span>
                            </p>
                        </div>
                        <div className="col-md-2">
                            <img src={bus.imageUrl || '/public/images/bus.png'} alt="Bus Image" className="img-fluid " style={{ width: '150px', height: '150px', borderRadius: '0.5rem' }} />
                        </div>
                    </div>
                </div>
            </div>

            {/* Driver Details Card */}
            <div className="card shadow-sm mb-4">
                <div className="card-header bg-info text-white d-flex justify-content-between align-items-center">
                    <h4 className="mb-0"><FaUser className="me-2" /> Driver Details</h4>
                    {driver ? (
                        <div>
                            <button
                                className="btn btn-sm btn-danger me-2"
                                onClick={() => deleteDriver(driver.id, busId)}
                            >
                                <FaTrash className="me-1" /> Remove
                            </button>
                            <Link
                                to={`/operator/edit-driver/${driver.id}`}
                                className="btn btn-sm btn-light "
                            >
                                <FaEdit className="me-1" /> Edit
                            </Link>
                            
                        </div>
                    ) : (
                        <Link
                            to={`/operator/add-driver/${busId}`}
                            className="btn btn-sm btn-success"
                        >
                            <FaPlus className="me-1" /> Add Driver
                        </Link>
                    )}
                </div>
                <div className="card-body">
                    {driver ? (
                        <div className="row">
                            <div className="col-md-5">
                                <p><strong><FaUser className="me-2" /> Name:</strong> {driver.name || 'N/A'}</p>
                                <p><strong><FaPhoneAlt className="me-2" /> Contact:</strong> {driver.contact || 'N/A'}</p>
                                <p><strong><FaMailBulk className="me-2" /> Email:</strong> {driver.email || 'N/A'}</p>
                            </div>
                            <div className="col-md-5">
                                <p><strong><FaIdCard className="me-2" /> License No:</strong> {driver.licenseNumber || 'N/A'}</p>
                                <p><strong><FaAward className="me-2" /> Experience:</strong> {driver.experience || '0'} years</p>
                            </div>
                            <div className="col-md-2">
                                <img src={driver.profilePic || '/public/images/bus.png'} alt="Driver Profile" className="img-fluid " style={{ width: '150px', height: '150px', borderRadius: '0.5rem' }} />
                            </div>
                        </div>
                    ) : (
                        <div className="text-center py-3">
                            <p className="text-muted">No driver assigned to this bus</p>
                            <Link
                                to={`/operator/add-driver/${busId}`}
                                className="btn btn-primary mt-2"
                            >
                                <FaPlus className="me-1" /> Assign Driver
                            </Link>
                        </div>
                    )}
                </div>
            </div>

            {/* Amenities Card */}
            <div className="card shadow-sm mb-4">
                <div className="card-header bg-warning text-white d-flex justify-content-between align-items-center">
                    <h4 className="mb-0"><FaWifi className="me-2" /> Amenities</h4>
                    <Link
                        to={`/operator/edit-amenities/${busId}`}
                        className="btn btn-sm btn-light"
                    >
                        <FaEdit className="me-1" /> Edit
                    </Link>
                </div>
                <div className="card-body">
                    {amenities.length > 0 ? (
                        <ul className="list-group list-group-flush">
                            {amenities.map((amenity, index) => (
                                <li key={index} className="list-group-item">
                                    <FaCheckCircle className="me-2 text-muted" />
                                    {amenity.trim()}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="text-muted">No amenities added yet</p>
                    )}
                </div>
            </div>

            {/* Quick Actions Card */}
            <div className="card shadow-sm">
                <div className="card-header bg-light">
                    <h5 className="mb-0">Quick Actions</h5>
                </div>
                <div className="card-body">
                    <div className="d-flex justify-content-around text-center">
                        <Link to={`/operator/bus-details/seats/${busId}`} className="btn btn-outline-primary">
                            <FaChair size={24} />
                            <div>Seat Layout</div>
                        </Link>
                        <Link to={`/operator/show-route/${busId}`} className="btn btn-outline-info">
                            <FaRoute size={24} />
                            <div>Route</div>
                        </Link>
                        <Link to={`/operator/schedule-bus/${busId}`} className="btn btn-outline-secondary">
                            <FaClock size={24} />
                            <div>Schedule</div>
                        </Link>
                        <Link to={`/operator/bus/${busId}/feedbacks`} className="btn btn-outline-success">
                            <FaCommentAlt size={24} />
                            <div>View Feedbacks</div>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default BusDetails;