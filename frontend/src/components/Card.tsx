import card from './Card.module.css';

interface CardProps{ 
   location: string
   backgroundImage: string
   cover?: string | number
   position?: string
}

const Card = ({ location, backgroundImage, cover, position }: CardProps) => {
  return(
    <div className={card.card} style={{ backgroundImage: `url('/backgroundImages/${backgroundImage}')`, backgroundSize:`${cover}`, backgroundPosition:`${position}` }}>
        <button className={card.button}> 
        <img src="/fork-and-knife-meal-svgrepo-com.svg" className={card.forkandknife}></img><span className={card.text}>ORDER {location.toUpperCase()}</span>
        </button>
    </div>
  );
}

export default Card;
