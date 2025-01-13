import navbar from './NavBar.module.css'
import activenavbar from './ActiveNavBar.module.css'


function ActiveNavBar ({ isActive, text }) {
  <li className={({isActive} ? navbar.navbaritemactive : navbar.navbaritem)}>{text}</li>
}

export default ActiveNavBar;
