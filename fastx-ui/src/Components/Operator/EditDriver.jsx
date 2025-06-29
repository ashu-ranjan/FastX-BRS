import axios from "axios";
import { useEffect, useState } from "react";
import { FaAward, FaEnvelope, FaIdCard, FaPhoneAlt, FaUser } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";

function EditDriver() {
    // This component is used to edit a driver's details.

    const { driverId, busId } = useParams();
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [contact, setContact] = useState("");
    const [licenseNumber, setLicenseNumber] = useState("");
    const [experience, setExperience] = useState("");


    useEffect(() => {
        const getDriverDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/fastx/api/driver/${driverId}`, {
                    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
                })
                const data = response.data;
                setName(data.name);
                setEmail(data.email);
                setContact(data.contact);
                setLicenseNumber(data.licenseNumber);
                setExperience(data.experience);
                console.log("Driver details fetched successfully:", data);

            } catch (error) {
                console.error("Error fetching driver details:", error); 
                
            }
        }
        getDriverDetails();
    },[driverId])

    const updateDriverDetails = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:8080/fastx/api/driver/update/${driverId}`,{
                name: name,
                email: email,
                contact: contact,
                licenseNumber: licenseNumber,
                experience: experience
            },
        {
            headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
        })
        alert("Driver details updated successfully");
        navigate(-1);
        
        } catch (error) {
            console.error("Error updating driver details:", error);
            
        }
    }

    return (
        <div className="edit-driver-container">
            <h2>Edit Driver Details</h2>
            <form onSubmit={updateDriverDetails}>
                <div className="form-group">
                    <label><FaUser /> Name</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                
                <div className="form-group">
                    <label><FaEnvelope /> Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                
                <div className="form-group">
                    <label><FaPhoneAlt /> Contact</label>
                    <input
                        type="tel"
                        value={contact}
                        onChange={(e) => setContact(e.target.value)}
                        required
                    />
                </div>
                
                <div className="form-group">
                    <label><FaIdCard /> License Number</label>
                    <input
                        type="text"
                        value={licenseNumber}
                        onChange={(e) => setLicenseNumber(e.target.value)}
                        required
                    />
                </div>
                
                <div className="form-group">
                    <label><FaAward /> Experience (Years)</label>
                    <input
                        type="number"
                        value={experience}
                        onChange={(e) => setExperience(e.target.value)}
                        min="0"
                        required
                    />
                </div>
                
                <button type="submit" className="submit-btn">
                    Update Driver
                </button>
            </form>
        </div>
    );
}

export default EditDriver