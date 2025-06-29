import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Customer/css/BusesScheduled.css';
import { useEffect, useState } from 'react';

function BusesScheduled() {
    const navigate = useNavigate();
    const [buses, setBuses] = useState([]);
    const [routeInfo, setRouteInfo] = useState({ origin: '', destination: '', date: '' });

    useEffect(() => {
        // Get parameters from URL
        const params = new URLSearchParams(window.location.search);
        const origin = params.get('origin');
        const destination = params.get('destination');
        const date = params.get('date');

        // Set route info
        setRouteInfo({ origin, destination, date });

        // Fetch buses
        axios.get('http://localhost:8080/fastx/api/schedules/search', {
            params: { origin, destination, date }
        })
        .then(res => setBuses(res.data))
        .catch(err => console.log('Error:', err));
    }, []);

    const handleViewSeats = (bus) => {
        navigate(`/customer/seats/${bus.busId}`, {
            state: {
                scheduleId: bus.scheduleId,
                journeyDate: routeInfo.date,
                origin: routeInfo.origin,
                destination: routeInfo.destination
            }
        });
    };

    const showBusCard = (bus) => (
        <div key={bus.scheduleId} className="bus-card">
            <div className="bus-name-type">
                <h3>{bus.busName}</h3>
                <p>{bus.busType.replace('_', ' ')}</p>
            </div>
            
            <div className="timing">
                <div>
                    <p>{bus.departureTime}</p>
                    <small>Departure</small>
                </div>
                <div>
                    <p>{bus.duration.replace('PT', '').replace('H', 'h ').replace('M', 'm')}</p>
                </div>
                <div>
                    <p>{bus.arrivalTime}</p>
                    <small>Arrival</small>
                </div>
            </div>
            
            <div className="price-seats">
                <p>₹{bus.fare}</p>
                <button onClick={() => handleViewSeats(bus)}>
                    View Seats
                </button>
            </div>
        </div>
    );

    return (
        <div className="buses-page">
            <h1>{routeInfo.origin} to {routeInfo.destination}</h1>
            <p>{new Date(routeInfo.date).toLocaleDateString('en-US', { 
                weekday: 'long', 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric' 
            })}</p>
            <p>{buses.length} buses available</p>
            
            <div className="bus-list">
                {buses.map(showBusCard)}
            </div>
        </div>
    );
}

export default BusesScheduled;