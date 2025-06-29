import { use, useEffect, useState } from 'react';
import '../Operator/css/OperatorNavbar.css';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { FaUser } from 'react-icons/fa';

function OperatorNavbar() {

    const navigate = useNavigate();
    const [profilePic, setProfilePic] = useState('');
    const [cacheBuster, setCacheBuster] = useState(Date.now());
    const logout = () => {
        localStorage.clear();
        // Redirect to login page after logout
        navigate("/");
    }

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/fastx/api/bus-operator/get/profile`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });
                setProfilePic(response.data.profilePic);
                setCacheBuster(Date.now());
            } catch (error) {
                console.error("Failed to fetch customer profile");
            }
        };

        fetchProfile();
    }, []);

    const avatarUrl = profilePic ? `/images/${profilePic}?t=${cacheBuster}` : null;

    return (
        <div className='operator-navbar'>
            {/* <!-- Navbar --> */}
            <nav className="navbar navbar-expand-lg bg-body-tertiary fixed-top">
                {/* <!-- Container wrapper --> */}
                <div className="container-fluid">
                    {/* <!-- Toggle button --> */}
                    <button
                        data-mdb-collapse-init
                        className="navbar-toggler"
                        type="button"
                        data-mdb-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent"
                        aria-expanded="false"
                        aria-label="Toggle navigation"
                    >
                        <i className="fas fa-bars"></i>
                    </button>

                    {/* <!-- Collapsible wrapper --> */}
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        {/* <!-- Navbar brand --> */}
                        <a className="navbar-brand mt-2 mt-lg-0" href="#" >
                            <img
                                src="/images/fxlogo.png"
                                height="20"
                                alt="MDB Logo"
                                loading="lazy"

                            />
                        </a>
                        {/* <!-- Left links --> */}
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <Link className="nav-link" to="/operator">Home</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/operator/buses">Stats</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/operator/routes">Routes</Link>
                            </li>
                        </ul>
                        {/* <!-- Left links --> */}
                    </div>
                    {/* <!-- Collapsible wrapper --> */}

                    {/* <!-- Right elements --> */}
                    <div className="d-flex align-items-center">
                        {/* <!-- Icon --> */}
                        <a className="nav-bookings d-flex align-items-center gap-2 me-3" href="#">
                            <span className="name">Bookings</span>
                            <i className="fas fa-ticket-alt"></i>
                        </a>

                        <a className="nav-help d-flex align-items-center gap-2 me-3" href="#">
                            <span className="name">Help</span>
                            <i className="fas fa-question-circle"></i>
                        </a>

                        <Link className="nav-bus d-flex align-items-center gap-2 me-3" to="/operator/add-bus">
                            <span className="name">Add Bus</span>
                            <i className="fas fa-bus"></i>
                        </Link>

                        {/* <!-- Avatar --> */}
                        <div className="dropdown">
                            <Link
                                data-mdb-dropdown-init
                                className="dropdown-toggle d-flex align-items-center hidden-arrow"
                                href="#"
                                id="navbarDropdownMenuAvatar"
                                role="button"
                                data-mdb-toggle="dropdown"
                                aria-expanded="false"
                            >
                                {avatarUrl ? (
                                    <img
                                        src={avatarUrl}
                                        className="rounded-circle"
                                        height="30"
                                        width="30"
                                        alt="Profile"
                                        loading="lazy"
                                    />
                                ) : (
                                    <FaUser size={24} className="text-secondary" />
                                )}
                            </Link>
                            <ul
                                className="dropdown-menu dropdown-menu-end"
                                aria-labelledby="navbarDropdownMenuAvatar"
                            >
                                <li>
                                    <Link className="dropdown-item" to="/operator/profile">My profile</Link>
                                </li>
                                <li>
                                    <Link className="dropdown-item" to="/operator/profile">Settings</Link>
                                </li>
                                <li>
                                    <Link className="dropdown-item" to="/" onClick={() => logout()}>Logout</Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                    {/* <!-- Right elements --> */}
                </div>
                {/* <!-- Container wrapper --> */}
            </nav>
            {/* <!-- Navbar --> */}
        </div>
    )
}

export default OperatorNavbar