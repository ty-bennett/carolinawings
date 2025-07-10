import menuItem from "../components/MenuItem.module.css";
import { BsFillBookmarkStarFill } from "react-icons/bs";
import { FaPepperHot } from "react-icons/fa";
import { LuVegan } from "react-icons/lu";


const MenuItem = ({ isVegetarian, isSpicy, isFavorite, name, description, price }) => {
  return (
    <>
      <div className={menuItem.cardcontainer}>
        <div className={menuItem.headercontainer}>
          <h3
            className={menuItem.header}
          >
            {isSpicy ? <FaPepperHot style={{ marginRight: '5px' }} /> : ""}
            {isFavorite ? <BsFillBookmarkStarFill style={{ marginRight: '5px' }} color="#bf9515" /> : ""}
            {isVegetarian ? <LuVegan style={{ marginRight: '5px' }} color="#2E6F40" /> : ""}
            {name.toUpperCase()}
          </h3>
          <p className={menuItem.price}>{price}</p>
        </div>
        <div className={menuItem.bodycontainer}>
          <p className={menuItem.description}>{description}</p>
        </div>

      </div>
    </>
  );
}



export default MenuItem;
