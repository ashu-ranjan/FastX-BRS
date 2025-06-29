import { useEffect } from "react";
import CustomerNavbar from "./CustomerNavbar";
import { Outlet, useNavigate } from "react-router-dom";
import '../Customer/css/CustomerDashboard.css';

function CustomerDashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/");
    }
  }, []);

  return (
    <div className="customer-dashboard-layout">
      <CustomerNavbar />
      <main className="customer-main-content">
        <Outlet />
      </main>
    </div>
  );
}

export default CustomerDashboard;