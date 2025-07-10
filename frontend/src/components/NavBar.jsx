import navbar from './NavBar.module.css';
import wood from '/backgroundImages/woodtexture.jpg';
import logo from '/carolinawingslogo.png';
import { NavLink } from 'react-router';


const NavBar = () => {
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
            <nav>
              <ul className={navbar.navbar}>
                <NavLink to="/" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Home</NavLink> 
                <NavLink to="/menu" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Menu</NavLink>  
                <NavLink to="/specials" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Specials</NavLink> 
                <NavLink to="/catering" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Catering</NavLink> 
                <NavLink to="/locations" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Locations</NavLink> 
                <NavLink to="/contact" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Contact</NavLink>
                <NavLink to="/login" className={({isActive}) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Login</NavLink>

              </ul>
            </nav>
          </div>
        </div>
      </div>
  );
}

export default NavBar;

