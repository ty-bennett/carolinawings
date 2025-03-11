import navbar from './NavBar.module.css';
import wood from '/backgroundImages/woodtexture.jpg'
import logo from '/carolinawingslogo.png'
import ActiveNavBar from './ActiveNavBar.jsx'
import {Link} from 'react-router'


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
                <Link to="/"><ActiveNavBar isActive={true} content="Home"/></Link> 
                <Link to="/menu"><ActiveNavBar content="Menu" /></Link>
                <Link to="/specials"><ActiveNavBar content="Specials" /></Link>
                <Link to="/catering"><ActiveNavBar content="Catering" /></Link>
                <Link to="/locations"><ActiveNavBar content="Locations" /></Link>
                <Link to="/contact"><ActiveNavBar content="Contact" /></Link>
              </ul>
            </nav>
          </div>
        </div>
      </div>
  );
}

export default NavBar;

