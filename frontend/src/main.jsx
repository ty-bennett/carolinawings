import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import './index.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './pages/Home.jsx';
import Menu from './pages/Menu.jsx';
import Specials from './pages/Specials.jsx';
import Catering from './pages/Catering.jsx';
import Locations from './pages/Locations.jsx';
import Contact from './pages/Contact.jsx';
import Login from './pages/Login.js';
import Register from './pages/Register.jsx';
import Companies from './pages/Companies.jsx';
import RestaurantDashboard from './pages/RestaurantDashboard.jsx';
import Unauthorized from './pages/Unauthorized.jsx';
import MenuDetails from './pages/MenuDetails.jsx';
import MenuPage from './pages/MenuPage.jsx';
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext'
import Order from './pages/Order';
import OrderMenu from './pages/OrderMenu';

export default function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/">
          <Route index element={<Home />} />
          <Route path="/menu" element={<Menu />} />
          <Route path="/specials" element={<Specials />} />
          <Route path="/catering" element={<Catering />} />
          <Route path="/locations" element={<Locations />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/admin/companies" element={<Companies />} />
          <Route path="/admin/restaurants/dashboard" element={<RestaurantDashboard />} />
          <Route path="/admin/restaurants/dashboard/menus/:menuId" element={<MenuDetails />} />
          <Route path="/admin/restaurants/dashboard/menus" element={<MenuPage />} />
          <Route path="/unauthorized" element={<Unauthorized />} />
          <Route path="/order" element={<Order />} />
          <Route path="/order/:restaurantId" element={<OrderMenu />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <CartProvider>
        <App />
      </CartProvider>
    </AuthProvider>
  </StrictMode>
)
