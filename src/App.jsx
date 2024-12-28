import { useReducer, useState } from "react";
import React from "react";
import './App.css';
import smokehouseBurger from "./assets/foodimages/smokehouse_burger.png";



function Header({ year }) {
  return (
    <header>
      <h1>Carolina Wings </h1>
      <p>Copyright {year}</p>
    </header>
  );
}

const items = [
  "Mac n cheese",
  "Salmon ",
  "Smokehouse Burger"
];

const itemsObjects = items.map((dish, i) => ({
  id: i,
  title: dish
}));

function Main({ dishes, status, onStatus}) {
  return (
    <>
    <div>
      <button onClick={() => onStatus(true)}>I want to be open</button>
      <h2>Welcome to carolina wings</h2>
      <h2>We are {status ? "open" : "closed"}</h2>
    </div>
    
    <main>
      <img src={smokehouseBurger} height={300} width="auto"/>
      <ul>
        {dishes.map((dish, i) => (
          <li key={dish.id} style={{ listStyleType:"none"}}>{dish.title}</li>
        ))}
      </ul>
    </main>
    </> 
  );
}

function App() {
//  const [openStatus, setOpenStatus] = useState(true);
  const [openStatus, toggle] = useReducer((status) => !status, true)

  useEffect(() => {
    console.log(`The restaurant is ${openStatus ? "open" : "closed"}`)
  });

  return (
    <div>
    <Header name="Ty" year={new Date().getFullYear()}/>
    <h2>The restaurant is currently{" "}{openStatus ? "Open" : "Closed"}.</h2>
    <button onClick={() => openStatus(!openStatus)}>{openStatus ? "Close" : "Open"} restaurant</button>
    <br/>
    <Main dishes={itemsObjects} openStatus={openStatus} onStatus={toggle}/>
    </div>
  );
}

export default App
