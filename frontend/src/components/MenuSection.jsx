import MenuItem from "../components/MenuItem"
import section from "../components/MenuSection.module.css";
import {useEffect, useState} from "react";

const MenuSection = () => {
  const [menuItems, setMenuItems] = useState([]) 
  const [isError, setIsError] = useState(false);
  const shareables = menuItems.filter(item => item.category === "Shareables")

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
  
  return(
    <>
      <div className={section.contentcontainer}>
        <img className={section.quesadilla} src="../public/foodimages/quesadilla.jpg"/>
        <h1 className={section.sectiontitle}>SHAREABLES</h1>
        <div className={section.cardcontainer}>   
          <ul className={section.rowcontainer}>
          {shareables.map(item => (
              <MenuItem
                key={item.id} 
                name={item.name}
                price={item.price}
                description={item.description}
                className={section.card}
              >
              </MenuItem>
          ))}
          </ul> 
        </div>
      </div>
    </>
  );
}

export default MenuSection;