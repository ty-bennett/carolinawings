import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import './index.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './pages/Home';
import Menu from './pages/Menu';
import Specials from './pages/Specials';
import Catering from './pages/Catering';
import Locations from './pages/Locations';
import Contact from './pages/Contact';
import Login from './pages/Login.js';
import Register from './pages/Register';
import AdminDashboard from './pages/AdminDashboard';
import Unauthorized from './pages/Unauthorized';
import MenuDetails from './pages/MenuDetails';
import MenuPage from './pages/MenuPage';
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext'
import Order from './pages/Order';
import OrderMenu from './pages/OrderMenu';
import Checkout from './pages/Checkout';
import OrderConfirmation from './pages/OrderConfirmation';
import OrderHistory from './pages/OrderHistory';
import OrderDetails from './pages/OrderDetails';
import PickupStation from './pages/PickupStation';

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
          <Route path="/admin/restaurants/dashboard" element={<AdminDashboard />} />
          <Route path="/admin/restaurants/dashboard/menus/:menuId" element={<MenuDetails />} />
          <Route path="/admin/restaurants/dashboard/menus" element={<MenuPage />} />
          <Route path="/unauthorized" element={<Unauthorized />} />
          <Route path="/order" element={<Order />} />
          <Route path="/order/:restaurantId" element={<OrderMenu />} />
          <Route path="/checkout" element={<Checkout />} />
          <Route path="/order-confirmation/:orderId" element={<OrderConfirmation />} />
          <Route path="/orders" element={<OrderHistory />} />
          <Route path="/orders/:orderId" element={<OrderDetails />} />
          <Route path="/pickup" element={<PickupStation />} />
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
