import menuItem from "../components/MenuItem.module.css";

const MenuItem = ({isFavorite, isVegetarian, name, description, price, periods }) =>
{
  return(
    <div className={menuItem.cardcontainer}>
      {isFavorite ? <img src="yellow_heart.png" width="64" height="64"/> : <img src=""/>}
      {isVegetarian ?
        <span>Ⓥ</span> : <span></span>}
      <span>{name.toUpperCase()}</span>
    </div>
  );
}

export default MenuItem;