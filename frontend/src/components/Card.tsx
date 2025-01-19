import card from './Card.module.css';

interface CardProps{ 
   location: string
   backgroundImagePath?: string
}

const Card = ({ location, backgroundImagePath }: CardProps) => {
  return(
    <div>
      <img src="/fork-and-knife-meal-svgrepo-com.svg" height={64} width={64}></img>
      <p></p>
    </div>
  )
}

export default Card;
