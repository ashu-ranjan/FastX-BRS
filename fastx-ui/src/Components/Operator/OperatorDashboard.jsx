import { useEffect } from "react";
import OperatorNavbar from "./OperatorNavbar";
import { Outlet, useNavigate } from "react-router-dom";
import '../Operator/css/OperatorDashboard.css';

function OperatorDashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/");
    }
  }, []);

  return (
    <div className="operator-dashboard-layout">
      <OperatorNavbar />
      <main className="operator-main-content">
        <Outlet />
      </main>
    </div>
  );
}

export default OperatorDashboard;