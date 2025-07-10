import MenuItem from "../components/MenuItem"
import section from "../components/MenuSection.module.css";
import {useEffect, useState} from "react";


const OrderMenuSection = () => { 
  const [menuItems, setMenuItems] = useState([]) 
  const [isError, setIsError] = useState(false);
  const shareables = menuItems.filter(item => item.category === "Shareables");
  const soupsAndSalads = menuItems.filter(item => item.category ==="Soups and Salads");
  const wingsAndTender = menuItems.filter(item => item.category ==="Wings and Tenders");
  const ribsBBQSeafood = menuItems.filter(item => item.category === "Ribs, BBQ, Seafood");
  const burgers = menuItems.filter(item => item.category === "Burgers");
  const sandwichesAndWrap = menuItems.filter(item => item.category === "Sandwiches And Wraps");
  const sideItems = menuItems.filter(item => item.cateogry === "Side Items");


  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/v1/menu');
        const data = await response.json(); 
        setMenuItems(data);
        console.log(data);
      } catch (error) {
        console.error("error fetching data!", error);
        setIsError(true);
      }  
    };

    fetchData();
  
  }, []);

  if(isError)
  {
    return (
      <h2 style={{color: "red"}}>Something went wrong...</h2>
    )
  }

  return (
    <>
      <div className={section.cardscontainer}>   
        <ul className={section.rowcontainer}>
          {shareables.map(item => (
            <MenuItem
              className={section.card}
              key={item.id} name={item.name}
              price={item.price}
              description={item.description}
            >
            </MenuItem>
            ))}
        </ul>
      </div>
    </>
  );
}  
  
export default OrderMenuSection;
