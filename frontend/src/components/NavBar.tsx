import navbar from './NavBar.module.css';
import wood from '/backgroundImages/woodtexture.jpg'
import logo from '/carolinawingslogo.png'
import ActiveNavBar from './ActiveNavBar.tsx'


function NavBar(){
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
                <ActiveNavBar isActive={true} content="Home"/> 
                <ActiveNavBar isActive={false} content="Menu" />
                <ActiveNavBar isActive={false} content="Lunch Specials" />
                <ActiveNavBar isActive={false} content="Nightly Specials" />
                <ActiveNavBar isActive={false} content="Catering" />
                <ActiveNavBar isActive={false} content="Locations" />
                <ActiveNavBar isActive={false} content="Contact" />
              </ul>
            </nav>
          </div>
        </div>
      </div>
  );
}

export default NavBar;

