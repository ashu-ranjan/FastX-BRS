import axios from "axios";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import '../Components/Register.css'; // Import your CSS for styling

const Register = () => {
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [form, setForm] = useState({
        name: "",
        email: "",
        designation: "",
        password: ""
    });

    const processChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const processRegister = async (e) => {
        e.preventDefault();
        const userData = {
            name: form.name,
            email: form.email,
            designation: form.designation,
            user: { password: form.password }
        };

        try {
            await axios.post("http://localhost:8080/fastx/api/executive/add", userData);
            alert("Registration successful! Redirecting to login...");
            setTimeout(() => navigate("/"), 1000);
        } catch (error) {
            console.error("Registration error:", error);
            alert("Registration failed. Please try again.");
        }
    };

    return (
        <div className="register-container">
            <div className="register-left">
                <div className="logo-wrapper">
                    <img src="/images/fxlogo.png" alt="FastX Logo" />
                    <div className="brand-name" style={{fontSize: "60px"}}>FastX</div>
                    <h2>Join with us Now!</h2>
                    <p className="tagline">FastX : Travel makes easy.</p>
                </div>
            </div>

            <div className="register-right">
                <form className="register-form" onSubmit={processRegister}>
                    <h2>Register</h2>

                    <div className="form-row">
                        <div className="form-group">
                            {/* <label>Full Name</label> */}
                            <input
                                type="text"
                                name="name"
                                placeholder="Enter your full name"
                                value={form.name}
                                onChange={processChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            {/* <label>Email Address</label> */}
                            <input
                                type="email"
                                name="email"
                                placeholder="Enter your email address"
                                value={form.email}
                                onChange={processChange}
                                required
                            />
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            {/* <label>Company Address</label> */}
                            <input
                                type="text"
                                name="designation"
                                placeholder="Enter your designation"
                                value={form.designation}
                                onChange={processChange}
                                required
                            />
                        </div>
                        <div className="form-group password-wrapper">
                            {/* <label>Password</label> */}
                            <input
                                type={showPassword ? "text" : "password"}
                                name="password"
                                placeholder="Enter your password"
                                value={form.password}
                                onChange={processChange}
                                required
                            />
                            <button type="button" onClick={() => setShowPassword(!showPassword)}>
                                {showPassword ? "👁️" : "👁️‍🗨️"}
                            </button>
                        </div>
                    </div>

                    <button type="submit" className="register-button">
                        Register
                    </button>

                    <div className="register-footer">
                        Already have an account? <Link to="/">Login</Link>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Register;
