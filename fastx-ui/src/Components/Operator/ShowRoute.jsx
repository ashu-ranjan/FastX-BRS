import axios from 'axios';
import { useEffect, useState } from 'react';
import { FaBusAlt, FaCalendarAlt, FaClock, FaMapMarkedAlt } from 'react-icons/fa';
import { useParams } from 'react-router-dom';

function ShowRoute() {
    const {busId} = useParams();
    const [schedules, setSchedules] = useState([]);

    useEffect(() => {
        const fetchSchedules = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/fastx/api/schedules/bus/${busId}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setSchedules(Array.isArray(response.data) ? response.data : [response.data]);
                console.log("Fetched schedules:", response.data);
            } catch (error) {
                console.error("Error fetching schedules:", error);
            }
        };
        fetchSchedules();
    },[])

    const formatTime = (timeString) => {
        if (!timeString) return 'N/A';
        const [hours, minutes] = timeString.split(':');
        const hour = parseInt(hours, 10);
        const ampm = hour >= 12 ? 'PM' : 'AM';
        const displayHour = hour % 12 || 12;
        return `${displayHour}:${minutes} ${ampm}`;
    };

  return (
        <div className="container mt-4">
            <h3 className="mb-4 text-primary">
                <FaBusAlt className="me-2" /> Bus Schedule Overview
            </h3>

            {schedules.map(schedule => (
                <div className="card shadow-sm mb-3" key={schedule.id}>
                    <div className="card-header bg-info d-flex justify-content-between">
                        <span><FaMapMarkedAlt className="me-2" />Route</span>
                        <strong>{schedule.busRoute?.origin || 'N/A'} → {schedule.busRoute?.destination || 'N/A'}</strong>
                    </div>
                    <div className="card-body">
                        <div className="row mb-2">
                            <div className="col-md-4">
                                <FaClock className="me-2 text-muted" />
                                <strong>Departure:</strong> {formatTime(schedule.departureTime)}
                            </div>
                            <div className="col-md-4">
                                <FaClock className="me-2 text-muted" />
                                <strong>Arrival:</strong> {formatTime(schedule.arrivalTime)}
                            </div>
                            <div className="col-md-4">
                                <FaCalendarAlt className="me-2 text-muted" />
                                <strong>Days:</strong> {schedule.scheduleDays || 'N/A'}
                            </div>
                        </div>
                        <div>
                            <strong>Fare:</strong> ₹{schedule.baseFare}
                        </div>
                    </div>
                </div>
            ))}

            {schedules.length === 0 && (
                <div className="alert alert-info mt-4 text-center">
                    No schedules available for this bus.
                </div>
            )}
        </div>
    );
}

export default ShowRoute