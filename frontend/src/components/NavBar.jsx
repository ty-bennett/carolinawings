import navbar from './NavBar.module.css';
import wood from '/backgroundImages/woodtexture.jpg';
import logo from '/carolinawingslogo.png';
import { NavLink } from 'react-router';
import { useState } from 'react';
import { useNavigate } from 'react-router';


const NavBar = () => {
  const userId = localStorage.getItem("id");
  const [showUserDropdown, setShowUserDropdown] = useState(false);
  const navigate = useNavigate();


  const toggleDropdown = () => setShowUserDropdown(!showUserDropdown);
  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");    
  };

  const name = localStorage.getItem("name");
  const id = localStorage.getItem("id");
  const roles = JSON.parse(localStorage.getItem("roles")) || [];

  return (
      <div className={navbar.gridparent}>
        <div className={navbar.gridchildlogo}> 
          <img src={logo} className={navbar.logo}></img>
        </div>
        <div className={navbar.gridchildimg}>
          <img src={wood} className={navbar.backgroundimg}></img>
        </div>
        <div className={navbar.gridchildbutton}>
          <button className={navbar.ordernow}>Order Now</button>
        </div>
        <div className={navbar.gridchildnav}>
          <div className={navbar.navbarposition}>
              <div className={navbar.left}></div>
              <div className={navbar.center}>
                <NavLink to="/" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Home</NavLink> 
                <NavLink to="/menu" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Menu</NavLink>  
                <NavLink to="/specials" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Specials</NavLink> 
                <NavLink to="/catering" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Catering</NavLink> 
                <NavLink to="/locations" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Locations</NavLink> 
                <NavLink to="/contact" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Contact</NavLink>
                {!id && ( 
                <NavLink to="/login" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Login</NavLink> 
                )}
                {(roles.includes("ROLE_MANAGER") ||
                  roles.includes("ROLE_RESTAURANTADMIN")) && (
                    <NavLink to="/admin/restaurants/dashboard" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Admin Panel</NavLink> 
                  )}
              </div>
              {id && (
              <div className={navbar.right}>
                <div className={navbar.userDropdownContainer}
                onClick={toggleDropdown}>
                  <span className={navbar.welcomeMessage}>Welcome, {name}</span> 
                
                {showUserDropdown && (
                  <div className={navbar.dropdownMenu}>
                    <button onClick={handleLogout} className={navbar.dropdownItem}>
                      Logout
                    </button>
                  </div>
                )}
                </div>
              </div>
              )}
          </div>
        </div>
      </div>
  );
}

export default NavBar;

