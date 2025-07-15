import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import NavBar from "../components/NavBar";
import Footer from "../components/Footer";

function MenuDetails() {
  const [menus, setMenus] = useState([]);
  const [error, setError] = useState(null);
  const [restaurantId, setRestaurantId] = useState("");
  const [menuItems, setMenuItems] = useState([]);


  const fetchMenus = async () => {
    try {
      const token = localStorage.getItem("token");
      setRestaurantId(localStorage.getItem("restaurants"));
      const res = await axios.get(`http://localhost:8080/admin/restaurants/${restaurantId}/menus`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.data || res.data.length === 0) {
        setError("No menu items found.");
        setMenus([]);
      } else {
        setMenus(res.data.content);
        setError(null);
      }
    } catch (err) {
      console.error(err);
      setError("Failed to load menu items.");
    }
  };

  useEffect(() => {
    fetchMenus();
  }, [restaurantId]);

  const handleFetchMenuItems = async (item) => {
    const list = item.menuItemsList;
    if(item)
      setMenuItems(list);
    else 
      setMenuItems([]);
  }

  return (
    <>
      <NavBar />
      <div className="min-h-screen bg-gray-100 p-6">
        <h1 className="text-3xl font-bold mb-4">Menu Items</h1>
        {error && <p className="text-red-500 mb-4">{error}</p>}

        {menus.length > 0 && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {menus.map((item) => (
              <div
                onClick={handleFetchMenuItems(item)}
                key={item.id}
                className="bg-white p-4 shadow-md rounded-lg hover:shadow-lg transition"
              >
                <h2 className="text-xl font-semibold">{item.name}</h2>
                <p className="text-gray-700">{item.description}</p>

              </div>
            ))}
          </div>
        )}
          {menuItems.length > 0 && (
            <div className="bg-white p-4 shadow-md rounded-lg hover:shadow-lg transition">
              {menuItems.map((item) => (
                <div
                key={item.id}
                className="bg-white p-4 shadow-md rounded-lg hover:shadow-lg transition"
                >
                  <h2 className="text-xl font-semibold">{item.name}</h2>
                  <p className="text-gray-700">{item.description}</p>
                </div>
              ))}
            </div>
          )}
      </div>
      <Footer />
    </>
  );
}

export default MenuDetails;
