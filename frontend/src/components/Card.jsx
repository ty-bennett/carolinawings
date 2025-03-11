import card from './Card.module.css';

const Card = ({ location, backgroundImage, cover, position, link }) => {
  return(
    <div className={card.card} style={{ backgroundImage: `url('/backgroundImages/${backgroundImage}')`, backgroundSize:`${cover}`, backgroundPosition:`${position}` }}>
        <a href={link}>
        <button className={card.button}> 
        <img src="/fork-and-knife-meal-svgrepo-com.svg" className={card.forkandknife}></img><span className={card.text}>ORDER {location.toUpperCase()}</span>
        </button>
        </a>
    </div>
  );
}

export default Card;
