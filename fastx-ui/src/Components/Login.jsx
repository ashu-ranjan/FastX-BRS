import axios from "axios";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaGoogle, FaFacebookF, FaTwitter } from "react-icons/fa";
import '../Components/Auth.css';

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [msg, setMsg] = useState("");
    const [rememberMe, setRememberMe] = useState(false);
    const navigate = useNavigate();

    const processLogin = async (e) => {
        e.preventDefault();
        const encodedString = window.btoa(username + ':' + password);

        try {
            const response = await axios.get('http://localhost:8080/fastx/api/user/token', {
                headers: { "Authorization": "Basic " + encodedString }
            });

            const token = response.data.token;
            localStorage.setItem('token', token);

            const details = await axios.get("http://localhost:8080/fastx/api/user/details", {
                headers: { "Authorization": "Bearer " + token }
            });

            localStorage.setItem('name', details.data.name);
            
            switch (details.data.user.role) {
                case "CUSTOMER": navigate("/customer"); break;
                case "OPERATOR": navigate("/operator"); break;
                case "EXECUTIVE": navigate("/executive"); break;
                default: setMsg("Login Disabled, Contact Admin");
            }
        } catch (error) {
            setMsg("Invalid Credentials");
        }
    }

    return (
        <div className="compact-login-container">
            <div className="left-content">
                <div className="logo-container">
                    <img src="/images/fxlogo.png" alt="FastX Logo" className="logo-image" />
                    <h1 className="brand-name" style={{ fontSize: "60px" , marginLeft: "20px"}}>FastX</h1>
                </div>
                <div className="welcome-text">
                    <h2>Welcomes you Back!</h2>
                    <p className="tagline">FastX : Travel makes easy.</p>

                </div>
            </div>

            <div className="right-form">
                <div className="form-container">
                    {/* <h2>Login</h2> */}
                    <form onSubmit={processLogin}>
                        <div className="form-group">
                            <label>Email address</label>
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Password</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-options">
                            <label className="checkbox-container">
                                <input
                                    type="checkbox"
                                    checked={rememberMe}
                                    onChange={() => setRememberMe(!rememberMe)}
                                />
                                <span className="checkmark"></span>
                                Remember me
                            </label>
                            <Link to="/forgot-password" className="forgot-password">
                                Forgot password?
                            </Link>
                        </div>

                        <button type="submit" className="login-button">
                            Login
                        </button>

                        <div className="social-login">
                            <p>Or sign in with</p>
                            <div className="social-icons">
                                <FaGoogle className="social-icon google" />
                                <FaFacebookF className="social-icon facebook" />
                                <FaTwitter className="social-icon twitter" />
                            </div>
                        </div>

                        <div className="register-link">
                            Don't have an account? <Link to="/customer-registration">Register</Link>
                            <br />
                            Join as an <Link to="/register">Operator</Link> or <Link to="/executive-registration">Executive</Link>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;
