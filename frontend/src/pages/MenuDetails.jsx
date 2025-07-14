import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import NavBar from "../components/NavBar";
import Footer from "../components/Footer";

function MenuDetails() {
  const { menuId } = useParams();
  const [menuItems, setMenuItems] = useState([]);
  const [error, setError] = useState(null);

  const fetchMenuItems = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get(`http://localhost:8080/admin/menus/${menuId}/menuitems`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.data || res.data.length === 0) {
        setError("No menu items found.");
        setMenuItems([]);
      } else {
        setMenuItems(res.data);
        setError(null);
      }
    } catch (err) {
      console.error(err);
      setError("Failed to load menu items.");
    }
  };

  useEffect(() => {
    fetchMenuItems();
  }, [menuId]);

  return (
    <>
      <NavBar />
      <div className="min-h-screen bg-gray-100 p-6">
        <h1 className="text-3xl font-bold mb-4">Menu Items</h1>
        {error && <p className="text-red-500 mb-4">{error}</p>}

        {menuItems.length > 0 && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {menuItems.map((item) => (
              <div
                key={item.id}
                className="bg-white p-4 shadow-md rounded-lg hover:shadow-lg transition"
              >
                <h2 className="text-xl font-semibold">{item.name}</h2>
                <p className="text-gray-700">{item.description}</p>
                <p className="text-green-600 font-medium mt-2">${item.price.toFixed(2)}</p>
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
