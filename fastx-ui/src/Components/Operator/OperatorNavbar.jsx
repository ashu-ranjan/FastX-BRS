import { use } from 'react';
import '../../css/OperatorNavbar.css';
import { Link, useNavigate } from 'react-router-dom';

function OperatorNavbar() {

    const navigate = useNavigate();
    const logout = () => {
        localStorage.clear();
        // Redirect to login page after logout
        navigate("/");
    }


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
                                src="../../public/images/fxlogo.png"
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


                        {/* Notifications Dropdown */}
                        {/* <div className="dropdown">
                            <a
                                className="nav-notification d-flex align-items-center gap-2 me-3 position-relative dropdown-toggle"
                                href="#"
                                id="navbarDropdownMenuLink"
                                role="button"
                                data-mdb-toggle="dropdown"
                                aria-expanded="false"
                            >
                                <span className="name">Notifications</span>
                                <i className="fas fa-bell"></i>
                                <span className="notification-badge">1</span>
                            </a>
                            <ul
                                className="dropdown-menu dropdown-menu-end"
                                aria-labelledby="navbarDropdownMenuLink"
                            >
                                <li><a className="dropdown-item" href="#">Some news</a></li>
                                <li><a className="dropdown-item" href="#">Another news</a></li>
                                <li><a className="dropdown-item" href="#">Something else here</a></li>
                            </ul>
                        </div> */}
                        {/* <!-- Avatar --> */}
                        <div className="dropdown">
                            <a
                                data-mdb-dropdown-init
                                className="dropdown-toggle d-flex align-items-center hidden-arrow"
                                href="#"
                                id="navbarDropdownMenuAvatar"
                                role="button"
                                data-mdb-toggle="dropdown"
                                aria-expanded="false"
                            >
                                <img
                                    src="https://mdbcdn.b-cdn.net/img/new/avatars/2.webp"
                                    className="rounded-circle"
                                    height="25"
                                    alt="Black and White Portrait of a Man"
                                    loading="lazy"
                                />
                            </a>
                            <ul
                                className="dropdown-menu dropdown-menu-end"
                                aria-labelledby="navbarDropdownMenuAvatar"
                            >
                                <li>
                                    <a className="dropdown-item" href="#">My profile</a>
                                </li>
                                <li>
                                    <a className="dropdown-item" href="#">Settings</a>
                                </li>
                                <li>
                                    <Link className="dropdown-item" to="/" onClick={()=>logout()}>Logout</Link>
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