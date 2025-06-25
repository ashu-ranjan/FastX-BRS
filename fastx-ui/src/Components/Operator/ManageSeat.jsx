import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Tag } from 'primereact/tag';
import { Button } from 'primereact/button';
import { InputNumber } from 'primereact/inputnumber';
import { Dropdown } from 'primereact/dropdown';
import '../../css/ManageSeat.css';
import { FaChair } from 'react-icons/fa';

function ManageSeat() {
  const { busId } = useParams();
  const [seats, setSeats] = useState([]);

  useEffect(() => {
    const fetchSeats = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/fastx/api/get/seats/bus/${busId}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        setSeats(response.data);
      } catch (error) {
        console.error('Error fetching seats:', error);
      }
    };

    fetchSeats();
  }, [busId]);

  const onRowEditComplete = async (e) => {
    const updatedSeats = [...seats];
    const { newData, index } = e;

    updatedSeats[index] = newData;
    setSeats(updatedSeats);

    try {
      await axios.put(
        `http://localhost:8080/fastx/api/update/seat/${newData.id}`,
        {
          price: parseFloat(newData.price),
          active: newData.active
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
          },
        }
      );
    } catch (err) {
      console.error('Error updating seat:', err);
    }
  };

  const statusEditor = (options) => (
    <Dropdown
      value={options.value}
      options={[
        { label: 'Active', value: true },
        { label: 'Inactive', value: false }
      ]}
      onChange={(e) => options.editorCallback(e.value)}
    />
  );

  const priceEditor = (options) => (
    <InputNumber
      value={options.value}
      onValueChange={(e) => options.editorCallback(e.value)}
      mode="currency"
      currency="INR"
      locale="en-IN"
    />
  );

  const statusBody = (rowData) => (
    <Tag
      value={rowData.active ? 'Active' : 'Inactive'}
      severity={rowData.active ? 'success' : 'danger'}
    />
  );

  return (
    <div className="seat-management-wrapper">
      <div className="seat-management">
        <div className="seat-management-header">
          <FaChair size={40} className="seat-icon text-primary mb-3" />
          Manage Seats {/* (Bus ID: {busId}) */}
        </div>

        <DataTable
          value={seats}
          scrollable
          scrollHeight="60vh"
          paginator
          rows={10}
          editMode="row"
          dataKey="id"
          onRowEditComplete={onRowEditComplete}
        >
          <Column field="seatNumber" header="Seat No" style={{ minWidth: '8rem' }} />
          <Column field="seatDeck" header="Deck" style={{ minWidth: '8rem' }} />
          <Column field="seatType" header="Type" style={{ minWidth: '8rem' }} />

          <Column
            field="price"
            header="Price"
            body={(rowData) => `₹${rowData.price}`}
            editor={priceEditor}
            style={{ minWidth: '10rem' }}
          />

          <Column
            field="active"
            header="Status"
            body={statusBody}
            editor={statusEditor}
            style={{ minWidth: '10rem' }}
          />

          <Column
            rowEditor
            header="Edit"
            bodyStyle={{ textAlign: 'center' }}
            style={{ minWidth: '6rem' }}
          />
        </DataTable>
      </div>
    </div>
  );
}

export default ManageSeat;
