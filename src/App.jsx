import React from "react";
import './App.css';
import smokehouseBurger from "./assets/foodimages/smokehouse_burger.png";



function Header({ name, year }) {
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

function Main({ dishes }) {

  return (
    <>
    <div>
      <h2>Welcome to caorlina wings</h2>
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

  return (
    <div>
    <Header name="Ty" year={new Date().getFullYear()}/>
    <Main dishes={itemsObjects}/>
    </div>
  );
}

export default App
