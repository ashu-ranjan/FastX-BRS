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




function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login/>}></Route>
        <Route path="/operator" element={<OperatorDashboard/>}>
          <Route index element={<Dashboard/>}></Route>
          <Route path="buses" element={<Buses/>}></Route>
          <Route path="routes" element={<BusRoutes/>}></Route>
          <Route path="add-bus" element={<AddBusComponent/>}></Route>
          <Route path="add-route" element={<AddRoute/>}></Route>
          <Route path='bus/details/:busId' element={<SeatLayout/>}></Route>
          <Route path='bus/details/:busId/add-seat' element={<AddSeat/>}></Route>
          <Route path='bus/details/:busId/manage-seat' element={<ManageSeat/>}></Route>
        </Route>
          
        <Route path="/register" element={<Register />}></Route>
      </Routes>
    </BrowserRouter>

  )
}

export default App