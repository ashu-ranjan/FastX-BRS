import 'primereact/resources/themes/lara-light-blue/theme.css'; // or any other theme
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import { BrowserRouter, Route, Routes } from "react-router-dom"
import Login from "./Components/Login"
import Register from "./Components/Register"
import OperatorDashboard from "./Components/Operator/OperatorDashboard"
import Dashboard from "./Components/Operator/Dashboard"
import Buses from "./Components/Operator/Stats"
import BusRoutes from "./Components/Operator/BusRoutes"
import AddBusComponent from "./Components/Operator/AddBus"
import AddRoute from './Components/Operator/AddRoute';
import SeatLayout from './Components/Operator/SeatLayout';
import AddSeat from './Components/Operator/AddSeat';
import ManageSeat from './Components/Operator/ManageSeat';
import BusDetails from './Components/Operator/BusDetails';
import AddDriver from './Components/Operator/AddDriver';
import EditDriver from './Components/Operator/EditDriver';
import EditBus from './Components/Operator/EditBus';
import ScheduleBus from './Components/Operator/ScheduleBus';
import ShowRoute from './Components/Operator/ShowRoute';
import CustomerDashboard from './Components/Customer/CustomerDashboard';
import CDashboard from './Components/Customer/CDashboard';
import BusesScheduled from './Components/Customer/BusesScheduled';
import GetSeats from './Components/Customer/GetSeats';
import PaymentPage from './Components/Customer/PaymentPage';
import MyBookings from './Components/Customer/MyBookings';
import CancelPassengers from './Components/Customer/CancelPassengers';
import RegisterCustomer from './Components/RegisterCustomer';
import ExecutiveRegister from './Components/ExecutiveRegister';
import ExecutiveDashboard from './Components/Executive/ExecutiveDashboard';
import EDashboard from './Components/Executive/EDashboard';
import CProfile from './Components/Customer/CProfile';
import OProfile from './Components/Operator/OProfile';
import EProfile from './Components/Executive/EProfile';




function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login/>}></Route>
        <Route path="/register" element={<Register />}></Route>
        <Route path='/customer-registration' element={<RegisterCustomer />} />
        <Route path="/executive-registration" element={<ExecutiveRegister />} />

        <Route path="/operator" element={<OperatorDashboard/>}>
          <Route index element={<Dashboard/>}></Route>
          <Route path="buses" element={<Buses/>}></Route>
          <Route path="routes" element={<BusRoutes/>}></Route>
          <Route path="add-bus" element={<AddBusComponent/>}></Route>
          <Route path="add-route" element={<AddRoute/>}></Route>
          <Route path='bus-details/seats/:busId' element={<SeatLayout/>}></Route>
          <Route path='bus/details/:busId/add-seat' element={<AddSeat/>}></Route>
          <Route path='bus/details/:busId/manage-seat' element={<ManageSeat/>}></Route>
          <Route path='bus-details/:busId' element={<BusDetails/>}></Route>
          <Route path='add-driver/:busId' element={<AddDriver/>}></Route>
          <Route path='edit-driver/:driverId' element={<EditDriver/>}></Route>
          <Route path='edit-bus/:busId' element={<EditBus/>}></Route>
          <Route path='schedule-bus/:busId' element={<ScheduleBus/>}></Route>
          <Route path='show-route/:busId' element={<ShowRoute/>}></Route>
          <Route path='profile' element={<OProfile/>}></Route>
        </Route>

        <Route path="/customer" element={<CustomerDashboard />}>
          <Route index element={<CDashboard />} />
          <Route path="buses" element={<BusesScheduled />} />
          <Route path="seats/:busId" element={<GetSeats />} />
          <Route path="payment" element={<PaymentPage />} />
          <Route path="bookings" element={<MyBookings />} />
          <Route path="cancel-booking" element={<CancelPassengers />} />
          <Route path="profile" element={<CProfile/>}></Route>
        </Route>

        <Route path="/executive" element={<ExecutiveDashboard />}>
          <Route index element={<EDashboard />} />
          <Route path="profile" element={<EProfile/>}></Route>
        </Route>

      </Routes>
    </BrowserRouter>

  )
}

export default App