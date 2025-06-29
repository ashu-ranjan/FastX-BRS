import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import "../Executive/css/ExecutiveDashboard.css";
import ExecutiveNavbar from "../Executive/ExecutiveNavbar";

function ExecutiveDashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/");
    }
  }, []);

  return (
    <div className="executive-dashboard-layout">
      <ExecutiveNavbar />
      <main className="executive-main-content">
        <Outlet />
      </main>
    </div>
  );
}

export default ExecutiveDashboard;