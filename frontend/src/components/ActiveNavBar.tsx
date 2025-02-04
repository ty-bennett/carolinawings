import navbar from './NavBar.module.css'
import React from 'react';

interface ActiveNavBarProps {
  isActive?: boolean;
  content: string;
}

const ActiveNavBar = ({ isActive, content }:ActiveNavBarProps) => {
  return (
  <li className={(isActive ? navbar.navbaritemactive : navbar.navbaritem)}>{content}</li>
  )
}

export default ActiveNavBar;
