import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Card } from 'primereact/card';
import { Tag } from 'primereact/tag';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { FaRoute, FaMapMarkerAlt, FaLocationArrow, FaRoad, FaPlus } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import '../Operator/css/busroute.css';

function BusRoutes() {
  const [routes, setRoutes] = useState([]);
  const [globalFilter, setGlobalFilter] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRoutes = async () => {
      try {
        const response = await axios.get("http://localhost:8080/fastx/api/bus-route/get-all", {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
        });
        setRoutes(response.data);
      } catch (error) {
        console.error("Error fetching routes:", error);
      }
    };
    fetchRoutes();
  }, []);

  return (
    <div className="bus-route-wrapper">
      <div className="bus-route-container">
        <Card
          title={
            <div className="card-header">
              <div className="title-with-icon">
                <FaRoute className="me-2" />
                Available Bus Routes
              </div>
              <span className="p-input-icon-left search-box">
                <i className="pi pi-search" />
                <InputText
                  value={globalFilter}
                  onChange={(e) => setGlobalFilter(e.target.value)}
                  placeholder="   Search"
                />
              </span>
            </div>
          }
        >
          <DataTable
            value={routes}
            paginator
            rows={10}
            stripedRows
            showGridlines
            scrollable
            scrollHeight="60vh"
            globalFilter={globalFilter}
            emptyMessage="No routes found."
            className="p-datatable-sm"
          >
            <Column field="id" header="Route ID" sortable />
            <Column
              field="origin"
              header="Origin"
              sortable
              body={(rowData) => (
                <span>
                  <FaMapMarkerAlt className="me-2 text-danger" />
                  {rowData.origin}
                </span>
              )}
            />
            <Column
              field="destination"
              header="Destination"
              sortable
              body={(rowData) => (
                <span>
                  <FaLocationArrow className="me-2 text-primary" />
                  {rowData.destination}
                </span>
              )}
            />
            <Column
              field="distance"
              header="Distance (km)"
              sortable
              body={(rowData) => (
                <Tag icon={<FaRoad />} value={`${rowData.distance} km`} severity="info" />
              )}
            />
          </DataTable>
        </Card>
      </div>

      <Button
        icon={<FaPlus />}
        className="add-route-button"
        onClick={() => navigate("/operator/add-route")}
        tooltip="Add Route"
        tooltipOptions={{ position: 'left' }}
      />
    </div>
  );
}

export default BusRoutes;
