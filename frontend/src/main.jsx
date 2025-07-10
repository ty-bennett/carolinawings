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
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import Companies from './pages/Companies.jsx';


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
            <Route path="/companies" element={<Companies />} />
          </Route>
        </Routes>
      </BrowserRouter>
  );
}

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>
)