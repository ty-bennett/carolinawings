import navbar from './NavBar.module.css'
import React from 'react';


const ActiveNavBar = ({ isActive, content }) => {
  return (
  <li className={(isActive ? navbar.navbaritemactive : navbar.navbaritem)}>{content}</li>
  )
}

export default ActiveNavBar;
