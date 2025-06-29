import axios from 'axios';
import React, { useState } from 'react';
import { FaUser, FaPhone, FaIdCard, FaAward, FaPlus, FaPhoneAlt, FaMailBulk } from 'react-icons/fa';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import '../Operator/css/AddDriver.css'; // Optional: your custom styles

function AddDriver() {
    const { busId } = useParams(); // Get busId from URL
    const [name, setName] = useState('');
    const [contact, setContact] = useState('');
    const [email, setEmail] = useState('');
    const [licenseNumber, setLicenseNumber] = useState('');
    const [experience, setExperience] = useState('');
    const navigate = useNavigate();

    const processSubmit = async (e) => {
        e.preventDefault();

        try {

            await axios.post(
                `http://localhost:8080/fastx/api/driver/add/${busId}`,
                {
                    name: name,
                    contact: contact,
                    email: email,
                    licenseNumber: licenseNumber,
                    experience: experience,

                },
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                }
            );
            alert('Driver added successfully');
            // Reset form
            setName('');
            setContact('');
            setEmail('');
            setLicenseNumber('');
            setExperience('');

            navigate(`/operator/bus-details/${busId}`); // Redirect to bus details page after adding driver

        } catch (error) {
            alert(error.response?.data?.message || 'Failed to add driver');
        }
    };

    return (
        <div className="simple-driver-form">
            <h3><FaPlus /> Add Driver to Bus #{busId}</h3>

            <form onSubmit={processSubmit}>
                <div className="form-group">
                    <label><FaUser /> Name</label>
                    <input
                        type="text"
                        name="name"
                        value={name}
                        onChange={($e) => setName($e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label><FaMailBulk /> Email</label>
                    <input
                        type="email"
                        name="email"
                        value={email}
                        onChange={($e) => setEmail($e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label><FaPhoneAlt /> Contact</label>
                    <input
                        type="tel"
                        name="contact"
                        value={contact}
                        onChange={($e) => setContact($e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label><FaIdCard /> License Number</label>
                    <input
                        type="text"
                        name="licenseNumber"
                        value={licenseNumber}
                        onChange={($e) => setLicenseNumber($e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label><FaAward /> Experience (Years)</label>
                    <input
                        type="number"
                        name="experience"
                        value={experience}
                        onChange={($e) => setExperience($e.target.value)}
                        required
                    />
                </div>

                <button type="submit" className="btn btn-primary">
                    Add Driver
                </button>
            </form>
        </div>
    );
}

export default AddDriver;