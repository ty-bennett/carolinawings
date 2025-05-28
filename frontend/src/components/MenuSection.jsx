import MenuItem from "../components/MenuItem"
import section from "../components/MenuSection";

const MenuSection = () => {
  const [menuItems, setMenuItems] = useState([]) 
  
  useEffect(() => {
    fetch('localhost:8080/api/v1/menu')
    .then(response => {
      if(!response.ok) {
        throw new Error('failed to fetch menu!');
      }
      return response.json();
    })
    .then(data => setMenuItems(data))
    .catch(error => console.error(error));
  }, []);

  return(
    <>
      <div className={section.contentcontainer}> 
        <h1 className={section.title}>Shareables</h1>
        <div className={section.rowcontainer}>   
        <ul>
			{menuItems.map(item => (
				<li key={item.id}>{item.name} - ${item.price}</li>
			))}
        </ul> 
        </div>
      </div>
    </>
  );
}

export default MenuSection;