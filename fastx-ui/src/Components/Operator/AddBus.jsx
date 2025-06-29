import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../Operator/css/AddBusComponent.css'; // Optional: your custom styles
import { useNavigate } from 'react-router-dom';
import { MultiSelect } from 'primereact/multiselect';


function AddBusComponent() {
    const navigate = useNavigate();
    const [busName, setBusName] = useState('');
    const [busNumber, setBusNumber] = useState('');
    const [busType, setBusType] = useState('');
    const [busTypes, setBusTypes] = useState([]);
    const [capacity, setCapacity] = useState('');
    const [amenities, setAmenities] = useState([]);
    // const [isActive, setIsActive] = useState(true);

    useEffect(() => {
        axios.get('http://localhost:8080/fastx/api/bus/get-bus-type', {
            headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
        })
            .then(response => {
                const types = Array.isArray(response.data) ? response.data : response.data.body;
                setBusTypes(types);
            })
            .catch(error => {
                console.error('Error fetching bus types', error);
                alert('Failed to fetch bus types');
            });
    }, []);


    const processSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('http://localhost:8080/fastx/api/bus/add', {
                busName: busName,
                busNumber: busNumber,
                busType: busType,
                capacity,
                amenities: amenities.join(', '),
                //    isActive
            }, {
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
            });
            alert('Bus added successfully');
            setBusName('');
            setBusNumber('');
            setBusType('');
            setCapacity('');
            setAmenities('');
            //    setIsActive(true);
        } catch (error) {
            console.error('Error adding bus', error);
            alert('Failed to add bus');
        }
        navigate('/operator'); // Redirect to the bus list or dashboard after adding
    };

    const amenityOptions = [
        { label: 'WiFi', value: 'wifi' },
        { label: 'Charging Port', value: 'chargingPort' },
        { label: 'Blanket', value: 'blanket' },
        { label: 'Pillow', value: 'pillow' },
        { label: 'Food Service', value: 'foodService' },
        { label: 'Entertainment System', value: 'entertainmentSystem' },
        { label: 'GPS Tracking', value: 'gpsTracking' },
        { label: 'Emergency Exit', value: 'emergencyExit' },
        { label: 'First Aid Kit', value: 'firstAidKit' },
        { label: 'Power Backup', value: 'powerBackup' },
        { label: 'Water Bottle', value: 'waterBottle' },
        { label: 'Luggage Storage', value: 'luggageStorage' },
        { label: 'Recliner Seats', value: 'reclinerSeats' },
        { label: 'Air Suspension', value: 'airSuspension' },
        { label: 'Fire Extinguisher', value: 'fireExtinguisher' },
        { label: 'CCTV Cameras', value: 'cctvCameras' }
    ];


    return (
        <div className="add-bus-container">
            <h3 className="mb-4 text-white">Add New Bus</h3>
            <form onSubmit={processSubmit} className="bg-light p-4 rounded shadow">
                <div className="row mb-3">
                    <div className="col-md-6">
                        <input type="text" className="form-control" placeholder="Bus Name"
                            value={busName} onChange={($e) => setBusName($e.target.value)} required />
                    </div>
                    <div className="col-md-6">
                        <input type="text" className="form-control" placeholder="Bus Number"
                            value={busNumber} onChange={($e) => setBusNumber($e.target.value)} required />
                    </div>
                </div>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <select
                            className="form-select"
                            value={busType}
                            onChange={($e) => setBusType($e.target.value)}
                            required
                        >
                            <option value="">Select Bus Type</option>
                            {busTypes.map((type, index) => (
                                <option key={index} value={type}>
                                    {type.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase())}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="col-md-6">
                        <input type="number" className="form-control" placeholder="Capacity"
                            value={capacity} onChange={($e) => setCapacity($e.target.value)} required />
                    </div>
                </div>
                <div className="mb-3">
                    {/* <label className="form-label fw-semibold">Select Amenities</label> */}
                    <div className="mb-3">
                        <MultiSelect
                            value={amenities}
                            options={amenityOptions}
                            onChange={(e) => setAmenities(e.value)}
                            placeholder="Select Amenities"
                            display="chip"
                            className="w-100"
                        />
                    </div>
                </div>
                {/* <div className="form-check form-switch mb-3">
                    <input className="form-check-input" type="checkbox" id="isActive"
                        checked={isActive} onChange={() => setIsActive(!isActive)} />
                    <label className="form-check-label" htmlFor="isActive">Is Active</label>
                </div> */}
                <button type="submit" className="btn btn-primary w-100">Add Bus</button>
            </form>
        </div>
    );
}

export default AddBusComponent;
