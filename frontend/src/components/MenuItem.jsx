import menuItem from "../components/MenuItem.module.css";

const MenuItem = ({isVegetarian, name, description, price}) =>
{
  return(
    <div className={menuItem.cardcontainer}>
      <div className={menuItem.headercontainer}>
        <h3 className={menuItem.header}>{name.toUpperCase()}</h3>
        <p className={menuItem.price}>{price}</p>
      </div>
      {isVegetarian ? <p>Ⓥ</p> : ""} 
      <p className={menuItem.description}>{description}</p>
    </div>
  );
}



export default MenuItem;