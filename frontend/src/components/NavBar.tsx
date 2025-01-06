import navbar from './NavBar.module.css';
import Link from 'react-dom'

function NavBar() {
  return (
    <main>
      <div>
        <nav>
          <ul className={navbar.navbar}>
            <li className={navbar.navbaritem}>Home</li>
            <li className={navbar.navbaritem}>Menu</li>
            <li className={navbar.navbaritem}>Lunch Specials</li>
            <li className={navbar.navbaritem}>Nightly Specials</li>
            <li className={navbar.navbaritem}>Catering</li>
            <li className={navbar.navbaritem}>Locations</li>
            <li className={navbar.navbaritem}>Contact</li>
          </ul>
        </nav>
      </div>
    </main>
  )
}

export default NavBar;

