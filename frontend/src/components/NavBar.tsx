import navbar from './NavBar.module.css';
import wood from '/backgroundImages/woodtexture.jpg'
import logo from '/carolinawingslogo.png'
import ActiveNavBar from './ActiveNavBar.tsx'


function NavBar(){
  return (
    <main>
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
                <ActiveNavBar isActive={true} text="Home"/> 
                <li className={navbar.navbaritem}>Menu</li>
                <li className={navbar.navbaritem}>Lunch Specials</li>
                <li className={navbar.navbaritem}>Nightly Specials</li>
                <li className={navbar.navbaritem}>Catering</li>
                <li className={navbar.navbaritem}>Locations</li>
                <li className={navbar.navbaritem}>Contact</li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </main>
  );
}

export default NavBar;

