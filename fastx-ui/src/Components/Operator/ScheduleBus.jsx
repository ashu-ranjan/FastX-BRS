import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { FaCalendarAlt, FaMapMarkerAlt } from "react-icons/fa";
import '../Operator/css/ScheduleBus.css'; // Assuming you have a CSS file for styling

function ScheduleBus() {
    const { busId } = useParams();
    const navigate = useNavigate();
    const [routes, setRoutes] = useState([]);
    const [origin, setOrigin] = useState("");
    const [destination, setDestination] = useState("");
    const [filteredDestinations, setFilteredDestinations] = useState([]);
    const [departureTime, setDepartureTime] = useState("");
    const [arrivalTime, setArrivalTime] = useState("");
    const [baseFare, setBaseFare] = useState("");
    const [scheduleDays, setScheduleDays] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const dayOptions = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY", "DAILY"];

    // Fetch all available routes
    useEffect(() => {
        const fetchRoutes = async () => {
            try {
                const response = await axios.get("http://localhost:8080/fastx/api/bus-route/get-all", {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setRoutes(response.data);
            } catch (error) {
                console.error("Error fetching routes:", error);
                setError("Failed to load routes. Please try again.");
            }
        };
        fetchRoutes();
    }, []);

    // Filter destinations based on selected origin
    useEffect(() => {
        if (origin) {
            const destinations = routes
                .filter(route => route.origin === origin)
                .map(route => route.destination);
            setFilteredDestinations([...new Set(destinations)]);
            setDestination("");
        } else {
            setFilteredDestinations([]);
            setDestination("");
        }
    }, [origin, routes]);

    const handleScheduleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            // Basic validation
            if (!origin || !destination) {
                throw new Error("Please select both origin and destination");
            }
            if (!departureTime || !arrivalTime) {
                throw new Error("Please select both departure and arrival times");
            }
            if (!baseFare || isNaN(baseFare)) {
                throw new Error("Please enter a valid base fare");
            }
            if (!scheduleDays) {
                throw new Error("Please select schedule days");
            }

            // Find routeId for selected origin-destination pair
            const selectedRoute = routes.find(
                route => route.origin === origin && route.destination === destination
            );

            if (!selectedRoute) {
                throw new Error("No route found for the selected origin and destination");
            }

            // Format time to ensure it has seconds
            const formatTime = (time) => {
                const parts = time.split(':');
                if (parts.length === 2) return `${time}:00`; // Add seconds if missing
                return time;
            };

            const scheduleData = {
                departureTime: formatTime(departureTime),
                arrivalTime: formatTime(arrivalTime),
                baseFare: parseFloat(baseFare),
                scheduleDays: scheduleDays === "DAILY" ? "DAILY" : scheduleDays.toUpperCase()
            };

            console.log("Submitting schedule data:", scheduleData); // Debug log

            const response = await axios.post(
                `http://localhost:8080/fastx/api/schedules/create/bus/${busId}/route/${selectedRoute.id}`,
                scheduleData,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json'
                    }
                }
            );

            if (response.status === 200 || response.status === 201) {
                alert('Bus scheduled successfully!');
                navigate(-1);
            } else {
                throw new Error(response.data?.message || "Failed to schedule bus");
            }
        } catch (error) {
            console.error("Detailed error:", {
                message: error.message,
                response: error.response?.data,
                request: error.config?.data
            });

            // Show specific error message from backend if available
            const errorMessage = error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to schedule bus. Please check all fields and try again.";

            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    // Get unique origins for dropdown
    const uniqueOrigins = [...new Set(routes.map(route => route.origin))];

    return (
        <div className="schedule-container">
            <h2><FaCalendarAlt /> Schedule Bus</h2>

            {error && (
                <div className="error-message" style={{ color: 'red', marginBottom: '15px' }}>
                    {error}
                </div>
            )}

            <form onSubmit={handleScheduleSubmit}>
                <div className="form-group">
                    <label><FaMapMarkerAlt /> Origin</label>
                    <select
                        value={origin}
                        onChange={(e) => setOrigin(e.target.value)}
                        required
                    >
                        <option value="">-- Select Origin --</option>
                        {uniqueOrigins.map((location, index) => (
                            <option key={`origin-${index}`} value={location}>
                                {location}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label><FaMapMarkerAlt /> Destination</label>
                    <select
                        value={destination}
                        onChange={(e) => setDestination(e.target.value)}
                        required
                        disabled={!origin}
                    >
                        <option value="">-- Select Destination --</option>
                        {filteredDestinations.map((location, index) => (
                            <option key={`dest-${index}`} value={location}>
                                {location}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Departure Time (HH:MM:SS)</label>
                    <input
                        type="time"
                        value={departureTime}
                        onChange={(e) => setDepartureTime(e.target.value)}
                        step="1"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Arrival Time (HH:MM:SS)</label>
                    <input
                        type="time"
                        value={arrivalTime}
                        onChange={(e) => setArrivalTime(e.target.value)}
                        step="1"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Base Fare</label>
                    <input
                        type="number"
                        value={baseFare}
                        onChange={(e) => setBaseFare(e.target.value)}
                        min="0"
                        step="0.01"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Schedule Days</label>
                    <select
                        value={scheduleDays}
                        onChange={(e) => setScheduleDays(e.target.value)}
                        required
                    >
                        <option value="">-- Select Day --</option>
                        {dayOptions.map(day => (
                            <option key={day} value={day}>
                                {day}
                            </option>
                        ))}
                    </select>
                </div>

                <button
                    type="submit"
                    className="submit-btn"
                    disabled={loading}
                    style={{ backgroundColor: loading ? '#ccc' : '#4CAF50' }}
                >
                    {loading ? 'Scheduling...' : 'Schedule Bus'}
                </button>
            </form>
        </div>
    );
}

export default ScheduleBus;