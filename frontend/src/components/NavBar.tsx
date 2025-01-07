import navbar from './NavBar.module.css';
import wood from '/backgroundImages/woodtexture.jpg'
import logo from '/carolinawingslogo.png'

function NavBar() {
  const isActive = true;
  return (
    <main>
      <div className={navbar.stickydiv}>
        <div className={navbar.flexcontainer}>
          <img src={logo} className={navbar.logo}></img>
          <img src={wood} className={navbar.img}></img>
          <button className={navbar.ordernow}>Order Now</button>
        </div>
        <div className={navbar.stickydiv}>
          <nav>
            <ul className={navbar.navbar}>
              <li className={(isActive ? navbar.navbaritemactive : navbar.navbaritem)}>Home</li>
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
    </main>
  )
}

export default NavBar;

