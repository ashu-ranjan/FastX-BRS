import { useEffect, useState } from 'react';
import '../Customer/css/CustomerNavbar.css';
import { Link, useNavigate } from 'react-router-dom';
import { FaUser } from 'react-icons/fa6';
import axios from 'axios';


function CustomerNavbar() {

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
                const response = await axios.get(`http://localhost:8080/fastx/api/customer/get/profile`, {
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
        <div className='customer-navbar'>
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
                        <Link className="navbar-brand mt-2 mt-lg-0" to="/customer">
                            <img
                                src="/images/fxlogo.png"
                                height="30"
                                alt="MDB Logo"
                                loading="lazy"

                            />
                        </Link>
                        {/* <!-- Left links --> */}
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <Link className="nav-link" to="/customer">Home</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/customer/about">About Us</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/customer/contact">Contact</Link>
                            </li>
                        </ul>
                        {/* <!-- Left links --> */}
                    </div>
                    {/* <!-- Collapsible wrapper --> */}

                    {/* <!-- Right elements --> */}
                    <div className="d-flex align-items-center">
                        {/* <!-- Icon --> */}
                        <Link className="nav-bookings d-flex align-items-center gap-2 me-3" to="/customer/bookings">
                            <span className="name">My Bookings</span>
                            <i className="fas fa-ticket-alt"></i>
                        </Link>

                        <Link className="nav-help d-flex align-items-center gap-2 me-3" to="/customer/help">
                            <span className="name">Help</span>
                            <i className="fas fa-question-circle"></i>
                        </Link>

                        <Link className="nav-bus d-flex align-items-center gap-2 me-3" to="/customer/offers">
                            <span className="name">All Offers</span>
                            <i className="fas fa-tags"></i>
                        </Link>

                        {/* <!-- Avatar --> */}
                        <div className="dropdown">
                            <Link
                                data-mdb-dropdown-init
                                className="dropdown-toggle d-flex align-items-center hidden-arrow"
                                to=""
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
                                    <Link className="dropdown-item" to={`/customer/profile`}>My profile</Link>
                                </li>
                                <li>
                                    <Link className="dropdown-item" to={`/customer/profile`}>Settings</Link>
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

export default CustomerNavbar;